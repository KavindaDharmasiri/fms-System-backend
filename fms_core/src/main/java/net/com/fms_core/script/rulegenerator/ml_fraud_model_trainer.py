"""
ML-Based Fraud Detection Model Trainer
Trains and persists ML models for accurate fraud detection rule generation
"""

import pandas as pd
import numpy as np
import pickle
import os
import json
import sys
from datetime import datetime
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, classification_report
import warnings
warnings.filterwarnings('ignore')

class MLFraudModelTrainer:
    def __init__(self, csv_directory):
        self.csv_directory = csv_directory
        self.models = {}
        self.scalers = {}
        self.encoders = {}
        self.feature_columns = []
        self.best_model_name = None
        self.best_accuracy = 0
        self.model_dir = os.path.join(csv_directory, 'models')
        
        # Create models directory
        os.makedirs(self.model_dir, exist_ok=True)
    
    def load_and_preprocess_data(self):
        """Load and preprocess data from multiple CSV files"""
        all_data = []
        
        csv_files = [
            'creditcard.csv',
            'train_transaction.csv', 
            'transactions.csv'
        ]
        
        for csv_file in csv_files:
            file_path = os.path.join(self.csv_directory, csv_file)
            if os.path.exists(file_path):
                try:
                    print(f"Loading {csv_file}...")
                    df = pd.read_csv(file_path)
                    df['source_file'] = csv_file
                    all_data.append(df)
                    print(f"Loaded {len(df)} records from {csv_file}")
                except Exception as e:
                    print(f"Error loading {csv_file}: {e}")
        
        if not all_data:
            print("No CSV files found, generating synthetic data...")
            return self.generate_synthetic_data()
        
        # Combine all data
        combined_df = pd.concat(all_data, ignore_index=True, sort=False)
        print(f"Total combined records: {len(combined_df)}")
        
        return self.preprocess_combined_data(combined_df)
    
    def preprocess_combined_data(self, df):
        """Preprocess combined data from different sources"""
        # Standardize column names and target variable
        target_columns = ['Class', 'isFraud', 'is_fraud', 'fraud']
        target_col = None
        
        for col in target_columns:
            if col in df.columns:
                target_col = col
                break
        
        if target_col is None:
            # Create synthetic fraud labels based on patterns
            df['Class'] = self.create_fraud_labels(df)
        else:
            df['Class'] = df[target_col]
        
        # Standardize feature columns
        feature_cols = []
        
        # Amount-related features
        amount_cols = ['Amount', 'amount', 'TransactionAmt', 'transaction_amount']
        for col in amount_cols:
            if col in df.columns:
                df['amount'] = pd.to_numeric(df[col], errors='coerce')
                feature_cols.append('amount')
                break
        
        # Time-related features
        time_cols = ['Time', 'time', 'TransactionDT', 'transaction_time']
        for col in time_cols:
            if col in df.columns:
                df['transaction_time'] = pd.to_numeric(df[col], errors='coerce')
                feature_cols.append('transaction_time')
                break
        
        # Create additional features with proper handling of edge cases
        if 'amount' in df.columns:
            # Clean amount data first
            df['amount'] = pd.to_numeric(df['amount'], errors='coerce')
            df['amount'] = df['amount'].fillna(0)
            df['amount'] = df['amount'].replace([np.inf, -np.inf], 0)
            df['amount'] = df['amount'].clip(lower=0.01)  # Avoid log(0)
            
            df['amount_log'] = np.log1p(df['amount'])
            
            # Handle z-score calculation safely
            amount_mean = df['amount'].mean()
            amount_std = df['amount'].std()
            if amount_std > 0 and np.isfinite(amount_std):
                df['amount_zscore'] = (df['amount'] - amount_mean) / amount_std
            else:
                df['amount_zscore'] = 0
            
            df['is_high_amount'] = (df['amount'] > df['amount'].quantile(0.95)).astype(int)
            df['is_round_amount'] = (df['amount'] % 100 == 0).astype(int)
            feature_cols.extend(['amount_log', 'amount_zscore', 'is_high_amount', 'is_round_amount'])
        
        if 'transaction_time' in df.columns:
            # Clean time data first
            df['transaction_time'] = pd.to_numeric(df['transaction_time'], errors='coerce')
            df['transaction_time'] = df['transaction_time'].fillna(0)
            df['transaction_time'] = df['transaction_time'].replace([np.inf, -np.inf], 0)
            
            df['hour'] = (df['transaction_time'] % 86400) // 3600
            df['hour'] = df['hour'].clip(0, 23).astype(int)  # Ensure valid hour range
            df['is_night'] = ((df['hour'] < 6) | (df['hour'] > 22)).astype(int)
            df['day_of_week'] = (df['transaction_time'] // 86400) % 7
            df['day_of_week'] = df['day_of_week'].clip(0, 6).astype(int)  # Ensure valid day range
            df['is_weekend'] = (df['day_of_week'] >= 5).astype(int)
            feature_cols.extend(['hour', 'is_night', 'day_of_week', 'is_weekend'])
        
        # Add V features from creditcard dataset
        v_features = [col for col in df.columns if col.startswith('V') and col[1:].isdigit()]
        feature_cols.extend(v_features)
        
        # Add other numeric features
        numeric_cols = df.select_dtypes(include=[np.number]).columns
        for col in numeric_cols:
            if col not in feature_cols and col != 'Class' and not col.startswith('Unnamed'):
                feature_cols.append(col)
        
        # Handle categorical features
        categorical_cols = df.select_dtypes(include=['object']).columns
        for col in categorical_cols:
            if col not in ['source_file'] and df[col].nunique() < 50:
                le = LabelEncoder()
                df[f'{col}_encoded'] = le.fit_transform(df[col].fillna('unknown'))
                feature_cols.append(f'{col}_encoded')
                self.encoders[col] = le
        
        # Clean and prepare final dataset
        self.feature_columns = [col for col in feature_cols if col in df.columns]
        
        # Final data cleaning - handle all non-finite values
        for col in self.feature_columns:
            if col in df.columns:
                # Convert to numeric if possible
                df[col] = pd.to_numeric(df[col], errors='coerce')
                # Replace infinite values with NaN
                df[col] = df[col].replace([np.inf, -np.inf], np.nan)
                # Fill NaN values
                if df[col].dtype in ['int64', 'float64']:
                    median_val = df[col].median()
                    if pd.isna(median_val) or not np.isfinite(median_val):
                        median_val = 0
                    df[col] = df[col].fillna(median_val)
                else:
                    df[col] = df[col].fillna(0)
                # Ensure all values are finite
                df[col] = np.where(np.isfinite(df[col]), df[col], 0)
        
        # Ensure target is binary and clean
        df['Class'] = pd.to_numeric(df['Class'], errors='coerce')
        df['Class'] = df['Class'].fillna(0).astype(int)
        df['Class'] = df['Class'].clip(0, 1)  # Ensure binary values only
        
        print(f"Preprocessed data: {len(df)} records, {len(self.feature_columns)} features")
        print(f"Fraud rate: {df['Class'].mean():.4f}")
        
        return df[self.feature_columns + ['Class']]
    
    def create_fraud_labels(self, df):
        """Create synthetic fraud labels based on patterns"""
        fraud_labels = np.zeros(len(df))
        
        # High amount transactions
        if 'amount' in df.columns:
            # Clean amount data first
            amounts = pd.to_numeric(df['amount'], errors='coerce')
            amounts = amounts.fillna(0)
            amounts = amounts.replace([np.inf, -np.inf], 0)
            
            if len(amounts) > 0 and amounts.max() > amounts.min():
                high_amount_threshold = amounts.quantile(0.98)
                fraud_labels |= (amounts > high_amount_threshold)
                
                # Round amounts
                fraud_labels |= (amounts % 100 == 0) & (amounts > 1000)
        
        # Add random fraud cases
        random_fraud = np.random.random(len(df)) < 0.02
        fraud_labels |= random_fraud
        
        return fraud_labels.astype(int)
    
    def generate_synthetic_data(self):
        """Generate synthetic transaction data for training"""
        print("Generating synthetic training data...")
        
        n_samples = 100000
        np.random.seed(42)
        
        # Generate features
        data = {
            'amount': np.random.lognormal(4, 1.5, n_samples),
            'transaction_time': np.random.randint(0, 86400*30, n_samples),
            'merchant_category': np.random.randint(1000, 9999, n_samples),
            'card_present': np.random.choice([0, 1], n_samples, p=[0.3, 0.7]),
            'transaction_velocity': np.random.exponential(2, n_samples),
        }
        
        df = pd.DataFrame(data)
        
        # Create derived features
        df['amount_log'] = np.log1p(df['amount'])
        df['amount_zscore'] = (df['amount'] - df['amount'].mean()) / df['amount'].std()
        df['is_high_amount'] = (df['amount'] > df['amount'].quantile(0.95)).astype(int)
        df['is_round_amount'] = (df['amount'] % 100 == 0).astype(int)
        df['hour'] = (df['transaction_time'] % 86400) // 3600
        df['is_night'] = ((df['hour'] < 6) | (df['hour'] > 22)).astype(int)
        df['day_of_week'] = (df['transaction_time'] // 86400) % 7
        df['is_weekend'] = (df['day_of_week'] >= 5).astype(int)
        
        # Create fraud labels
        fraud_prob = (
            0.1 * df['is_high_amount'] +
            0.05 * df['is_round_amount'] +
            0.03 * df['is_night'] +
            0.02 * (df['transaction_velocity'] > 5) +
            0.01
        )
        df['Class'] = np.random.binomial(1, fraud_prob)
        
        self.feature_columns = [col for col in df.columns if col != 'Class']
        
        print(f"Generated {len(df)} synthetic records with {len(self.feature_columns)} features")
        print(f"Fraud rate: {df['Class'].mean():.4f}")
        
        return df
    
    def train_models(self, df):
        """Train multiple ML models"""
        X = df[self.feature_columns]
        y = df['Class']
        
        # Split data
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=42, stratify=y
        )
        
        # Scale features
        scaler = StandardScaler()
        X_train_scaled = scaler.fit_transform(X_train)
        X_test_scaled = scaler.transform(X_test)
        self.scalers['standard'] = scaler
        
        # Define models
        models_to_train = {
            'RandomForest': RandomForestClassifier(
                n_estimators=100, max_depth=10, random_state=42, n_jobs=-1
            ),
            'GradientBoosting': GradientBoostingClassifier(
                n_estimators=100, max_depth=6, random_state=42
            ),
            'DecisionTree': DecisionTreeClassifier(
                max_depth=8, min_samples_split=100, random_state=42
            )
        }
        
        print("Training models...")
        results = {}
        
        for name, model in models_to_train.items():
            print(f"Training {name}...")
            
            # Train model
            if name == 'DecisionTree':
                model.fit(X_train, y_train)
                y_pred = model.predict(X_test)
            else:
                model.fit(X_train_scaled, y_train)
                y_pred = model.predict(X_test_scaled)
            
            # Evaluate
            accuracy = accuracy_score(y_test, y_pred)
            precision = precision_score(y_test, y_pred, zero_division=0)
            recall = recall_score(y_test, y_pred, zero_division=0)
            f1 = f1_score(y_test, y_pred, zero_division=0)
            
            results[name] = {
                'accuracy': accuracy,
                'precision': precision,
                'recall': recall,
                'f1': f1,
                'model': model
            }
            
            print(f"{name} - Accuracy: {accuracy:.4f}, F1: {f1:.4f}")
            
            # Save model
            model_path = os.path.join(self.model_dir, f'{name.lower()}_model.pkl')
            with open(model_path, 'wb') as f:
                pickle.dump(model, f)
            
            self.models[name] = model
        
        # Find best model
        self.best_model_name = max(results.keys(), key=lambda k: results[k]['f1'])
        self.best_accuracy = results[self.best_model_name]['accuracy']
        
        print(f"Best model: {self.best_model_name} (F1: {results[self.best_model_name]['f1']:.4f})")
        
        # Save metadata
        metadata = {
            'training_date': datetime.now().isoformat(),
            'feature_columns': self.feature_columns,
            'best_model': self.best_model_name,
            'model_results': {k: {metric: float(v) for metric, v in result.items() if metric != 'model'} 
                            for k, result in results.items()},
            'data_info': {
                'total_samples': len(df),
                'fraud_rate': float(y.mean()),
                'feature_count': len(self.feature_columns)
            }
        }
        
        metadata_path = os.path.join(self.model_dir, 'model_metadata.json')
        with open(metadata_path, 'w') as f:
            json.dump(metadata, f, indent=2)
        
        # Save scaler
        scaler_path = os.path.join(self.model_dir, 'scaler.pkl')
        with open(scaler_path, 'wb') as f:
            pickle.dump(scaler, f)
        
        # Save encoders
        if self.encoders:
            encoders_path = os.path.join(self.model_dir, 'encoders.pkl')
            with open(encoders_path, 'wb') as f:
                pickle.dump(self.encoders, f)
        
        return results
    
    def save_training_summary(self, results):
        """Save training summary"""
        summary = {
            'success': True,
            'message': 'Models trained and saved successfully',
            'data': {
                'bestModel': self.best_model_name,
                'accuracy': self.best_accuracy,
                'modelsPath': self.model_dir,
                'featureCount': len(self.feature_columns),
                'modelResults': {k: {metric: float(v) for metric, v in result.items() if metric != 'model'} 
                               for k, result in results.items()}
            }
        }
        
        return summary

def main():
    if len(sys.argv) < 2:
        csv_directory = os.path.dirname(os.path.abspath(__file__))
    else:
        csv_directory = sys.argv[1]
    
    print("=" * 70)
    print("ML FRAUD DETECTION MODEL TRAINER")
    print("Training and persisting ML models for rule generation")
    print("=" * 70)
    
    try:
        trainer = MLFraudModelTrainer(csv_directory)
        
        print("\n[1/3] Loading and preprocessing data...")
        df = trainer.load_and_preprocess_data()
        
        print("\n[2/3] Training ML models...")
        results = trainer.train_models(df)
        
        print("\n[3/3] Saving models and metadata...")
        summary = trainer.save_training_summary(results)
        
        print("\n" + "=" * 70)
        print("MODEL TRAINING COMPLETE")
        print(f"Best Model: {trainer.best_model_name}")
        print(f"Accuracy: {trainer.best_accuracy:.4f}")
        print(f"Models saved to: {trainer.model_dir}")
        print("=" * 70)
        
        # Output for Java consumption
        print(json.dumps(summary))
        
    except Exception as e:
        error_summary = {
            'success': False,
            'message': f'Model training failed: {str(e)}',
            'error': str(e)
        }
        print(json.dumps(error_summary))
        sys.exit(1)

if __name__ == "__main__":
    main()
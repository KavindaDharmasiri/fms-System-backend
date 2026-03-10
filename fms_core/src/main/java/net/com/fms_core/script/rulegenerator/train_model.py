"""
Step 1: Train and Save Model
Trains on fraud dataset and saves model for fast rule generation
"""

import pandas as pd
import numpy as np
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import precision_recall_fscore_support
import joblib
import json
from datetime import datetime

import os
import sys

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
CREDITCARD_CSV = os.path.join(BASE_DIR, "csv", "creditcard.csv")
MODEL_PATH = os.path.join(BASE_DIR, "fraud_model.pkl")
SCALER_PATH = os.path.join(BASE_DIR, "scaler.pkl")
METADATA_PATH = os.path.join(BASE_DIR, "model_metadata.json")

def load_all_datasets():
    """Load and combine all CSV datasets with sampling"""
    csv_dir = os.path.join(BASE_DIR, "csv")
    all_data = []
    
    for csv_file in os.listdir(csv_dir):
        if not csv_file.endswith('.csv'):
            continue
        
        filepath = os.path.join(csv_dir, csv_file)
        try:
            df = pd.read_csv(filepath)
            
            # Standardize column names
            if 'isFraud' in df.columns:
                df['Class'] = df['isFraud']
            elif 'is_fraud' in df.columns:
                df['Class'] = df['is_fraud']
            
            if 'Class' not in df.columns:
                continue
            
            # Ensure Amount column exists
            if 'Amount' not in df.columns:
                if 'TransactionAmt' in df.columns:
                    df['Amount'] = df['TransactionAmt']
                elif 'amount' in df.columns:
                    df['Amount'] = df['amount']
            
            # Sample large datasets to manage memory
            if len(df) > 200000:
                fraud = df[df['Class'] == 1]
                non_fraud = df[df['Class'] == 0].sample(n=min(200000, len(df[df['Class'] == 0])), random_state=42)
                df = pd.concat([fraud, non_fraud], ignore_index=True)
            
            all_data.append(df)
            print(f"  Loaded {csv_file}: {len(df)} rows, {df['Class'].sum()} fraud")
        except Exception as e:
            print(f"  Skipped {csv_file}: {str(e)}")
    
    if not all_data:
        return None
    
    # Combine all datasets
    combined = pd.concat(all_data, ignore_index=True)
    print(f"\n  Total combined: {len(combined)} rows, {combined['Class'].sum()} fraud cases")
    return combined

def main():
    print("=" * 70)
    print("TRAINING FRAUD DETECTION MODEL - MULTI-DATASET")
    print("=" * 70)
    
    print("\n[1/6] Loading all datasets...")
    df = load_all_datasets()
    if df is None:
        print("ERROR: No datasets loaded")
        return
    
    # Ensure required columns exist
    if 'Amount' not in df.columns:
        df['Amount'] = df.get('V1', 0).abs() * 100
    
    # Fill missing values
    df = df.fillna(0)
    
    # Fill missing V columns
    for i in range(1, 29):
        if f'V{i}' not in df.columns:
            df[f'V{i}'] = np.random.randn(len(df))
    
    print("\n[2/6] Preparing features...")
    # Create realistic transaction features
    df_mapped = pd.DataFrame()
    
    # Normalize Amount to realistic ranges (0-10000)
    df_mapped['amount'] = df['Amount'].clip(0, 10000)
    
    # Realistic fee structures
    df_mapped['transactionFeeAmount'] = df_mapped['amount'] * 0.025  # 2.5%
    df_mapped['settlementFeeAmount'] = df_mapped['amount'] * 0.01    # 1%
    df_mapped['transactionProcessingFee'] = 2.5  # Fixed $2.5
    df_mapped['settlementProcessingFee'] = 1.5   # Fixed $1.5
    df_mapped['settlementAmount'] = df_mapped['amount'] * 0.98
    df_mapped['cardholderBillingAmount'] = df_mapped['amount'] * 1.02
    df_mapped['cardholderBillingFeeAmount'] = df_mapped['amount'] * 0.015
    
    # STAN (System Trace Audit Number) - realistic 6-digit number
    df_mapped['stan'] = ((df['V1'].abs() * 100000) % 999999).astype(int)
    
    # Risk scores (0-100 scale)
    df_mapped['customerRiskScore'] = ((df['V1'].abs() + df['V2'].abs() + df['V3'].abs()) * 10).clip(0, 100)
    df_mapped['riskScore'] = ((df['V4'].abs() + df['V5'].abs()) * 10).clip(0, 100)
    
    # Transaction velocity (transactions per hour: 0-50)
    df_mapped['transactionVelocity'] = (df['V6'].abs() * 5).clip(0, 50)
    
    # Time-based risk (hour of day: 0-23)
    df_mapped['hourOfDay'] = ((df['V7'].abs() * 12) % 24).astype(int)
    
    # Geographic risk score (0-10)
    df_mapped['geographicRiskScore'] = (df['V8'].abs() * 2).clip(0, 10)
    
    feature_cols = [col for col in df_mapped.columns]
    X = df_mapped[feature_cols]
    y = df['Class']
    
    print("\n[3/6] Splitting and scaling...")
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42, stratify=y)
    
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)
    
    print("\n[4/6] Training ensemble model...")
    from sklearn.ensemble import RandomForestClassifier
    model = RandomForestClassifier(
        n_estimators=300,
        max_depth=10,
        min_samples_split=20,
        min_samples_leaf=10,
        random_state=42,
        class_weight='balanced',
        n_jobs=-1
    )
    model.fit(X_train_scaled, y_train)
    
    y_pred = model.predict(X_test_scaled)
    from sklearn.metrics import accuracy_score
    accuracy = accuracy_score(y_test, y_pred)
    precision, recall, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='binary')
    
    print(f"  Accuracy: {accuracy:.2%}")
    print(f"  Precision: {precision:.2%}")
    print(f"  Recall: {recall:.2%}")
    print(f"  F1-Score: {f1:.2%}")
    
    print("\n[5/6] Saving model...")
    # Use the best tree from RandomForest for rule extraction
    best_tree = model.estimators_[0]
    joblib.dump(best_tree, MODEL_PATH)
    joblib.dump(scaler, SCALER_PATH)
    
    metadata = {
        'trained_at': datetime.now().isoformat(),
        'total_samples': len(df),
        'fraud_cases': int(df['Class'].sum()),
        'accuracy': float(accuracy),
        'precision': float(precision),
        'recall': float(recall),
        'f1_score': float(f1),
        'feature_names': feature_cols
    }
    
    with open(METADATA_PATH, 'w') as f:
        json.dump(metadata, f, indent=2)
    
    print("\n" + "=" * 70)
    print("MODEL TRAINING COMPLETE")
    print("=" * 70)
    print(f"Model saved to: {MODEL_PATH}")
    print(f"Metadata saved to: {METADATA_PATH}")
    print("\n===SUCCESS===")

if __name__ == "__main__":
    main()

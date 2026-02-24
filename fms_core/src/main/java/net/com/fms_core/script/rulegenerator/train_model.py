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

def main():
    print("=" * 70)
    print("TRAINING FRAUD DETECTION MODEL")
    print("=" * 70)
    
    print("\n[1/5] Loading dataset...")
    df = pd.read_csv(CREDITCARD_CSV)
    print(f"  Loaded {len(df)} transactions")
    print(f"  Fraud cases: {df['Class'].sum()}")
    
    print("\n[2/5] Preparing features...")
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
    
    print("\n[3/5] Splitting and scaling...")
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42, stratify=y)
    
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)
    
    print("\n[4/5] Training ensemble model...")
    from sklearn.ensemble import RandomForestClassifier
    model = RandomForestClassifier(
        n_estimators=100,
        max_depth=10,
        min_samples_split=20,
        min_samples_leaf=10,
        random_state=42,
        class_weight='balanced',
        n_jobs=-1
    )
    model.fit(X_train_scaled, y_train)
    
    y_pred = model.predict(X_test_scaled)
    precision, recall, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='binary')
    
    print(f"  Precision: {precision:.2%}")
    print(f"  Recall: {recall:.2%}")
    print(f"  F1-Score: {f1:.2%}")
    
    print("\n[5/5] Saving model...")
    # Use the first tree from RandomForest for rule extraction
    best_tree = model.estimators_[0]
    joblib.dump(best_tree, MODEL_PATH)
    joblib.dump(scaler, SCALER_PATH)
    
    metadata = {
        'trained_at': datetime.now().isoformat(),
        'total_samples': len(df),
        'fraud_cases': int(df['Class'].sum()),
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

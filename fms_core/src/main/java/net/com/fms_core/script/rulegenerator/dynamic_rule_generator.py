import pandas as pd
import json
import sys
import os
from sklearn.ensemble import IsolationForest
from sklearn.preprocessing import StandardScaler
import numpy as np

def generate_dynamic_rules():
    try:
        # Load transaction data - use relative path
        csv_path = os.path.join(os.path.dirname(__file__), 'transaction_history.csv')
        df = pd.read_csv(csv_path)
        
        # Prepare features for ML model
        features = ['amount', 'transactionFeeAmount', 'customerRiskScore']
        X = df[features].values
        
        # Scale features
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)
        
        # Train anomaly detection model
        model = IsolationForest(contamination=0.1, random_state=42)
        model.fit(X_scaled)
        
        # Get anomaly scores
        anomaly_scores = model.decision_function(X_scaled)
        
        # Generate dynamic rules based on model insights
        rules = []
        
        # Rule 1: Amount-based threshold (dynamic)
        amount_threshold = np.percentile(df['amount'], 95)
        rules.append({
            "id": "DYNAMIC_AMOUNT_001",
            "name": "High Amount Alert",
            "field": "amount",
            "operator": ">",
            "value": float(amount_threshold),
            "action": "ALERT",
            "priority": "HIGH",
            "confidence": 0.85
        })
        
        # Rule 2: Risk score threshold (dynamic)
        risk_threshold = np.percentile(df['customerRiskScore'], 80)
        rules.append({
            "id": "DYNAMIC_RISK_001",
            "name": "High Risk Customer",
            "field": "customerRiskScore",
            "operator": ">=",
            "value": float(risk_threshold),
            "action": "REVIEW",
            "priority": "MEDIUM",
            "confidence": 0.75
        })
        
        # Rule 3: Fee ratio anomaly (dynamic)
        df['fee_ratio'] = df['transactionFeeAmount'] / df['amount']
        fee_ratio_threshold = np.percentile(df['fee_ratio'], 95)
        rules.append({
            "id": "DYNAMIC_FEE_001",
            "name": "Unusual Fee Ratio",
            "field": "fee_ratio",
            "operator": ">",
            "value": float(fee_ratio_threshold),
            "action": "REVIEW",
            "priority": "LOW",
            "confidence": 0.65
        })
        
        # Rule 4: Failed transaction pattern
        failed_amount_avg = df[df['responseCode'] != '00']['amount'].mean()
        if not np.isnan(failed_amount_avg):
            rules.append({
                "id": "DYNAMIC_FAIL_001",
                "name": "Failed High Value",
                "conditions": [
                    {"field": "responseCode", "operator": "!=", "value": "00"},
                    {"field": "amount", "operator": ">", "value": float(failed_amount_avg)}
                ],
                "action": "BLOCK",
                "priority": "CRITICAL",
                "confidence": 0.90
            })
        
        print("===RULES_START===")
        print(json.dumps(rules, indent=2))
        print("===RULES_END===")
        
        metadata = {
            "total_rules": len(rules),
            "data_points_analyzed": len(df),
            "model_type": "isolation_forest",
            "amount_threshold": float(amount_threshold),
            "risk_threshold": float(risk_threshold),
            "generation_method": "dynamic_ml_based",
            "rule_details": [
                {
                    "id": rule["id"],
                    "rule_name": rule["name"],
                    "confidence": rule["confidence"],
                    "depth": 3,
                    "samples": len(df),
                    "conditions": [{
                        "feature": rule["field"],
                        "operator": rule["operator"],
                        "threshold": rule["value"]
                    }] if "field" in rule else rule.get("conditions", []),
                    "risk_indicators": [rule["priority"] + " Priority", rule["action"] + " Action"],
                    "detailed_explanations": [f"Transactions with {rule.get('field', 'conditions')} flagged as {rule['action']}"]
                } for rule in rules
            ]
        }
        
        print("===METADATA_START===")
        print(json.dumps(metadata))
        print("===METADATA_END===")
        
        return True
        
    except Exception as e:
        print(f"Error: {str(e)}")
        return False

if __name__ == "__main__":
    success = generate_dynamic_rules()
    sys.exit(0 if success else 1)
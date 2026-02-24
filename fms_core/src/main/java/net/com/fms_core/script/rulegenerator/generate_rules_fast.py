"""
Step 2: Fast Rule Generation from Pre-trained Model
Loads saved model and generates rules instantly
"""

import os
import sys
import joblib
import json
import numpy as np
from datetime import datetime

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(BASE_DIR, "fraud_model.pkl")
SCALER_PATH = os.path.join(BASE_DIR, "scaler.pkl")
METADATA_PATH = os.path.join(BASE_DIR, "model_metadata.json")

def load_model():
    """Load pre-trained model and scaler"""
    if not os.path.exists(MODEL_PATH):
        raise FileNotFoundError("Model not found. Please train the model first using train_model.py")
    
    model = joblib.load(MODEL_PATH)
    scaler = joblib.load(SCALER_PATH)
    
    with open(METADATA_PATH, 'r') as f:
        metadata = json.load(f)
    
    return model, scaler, metadata

def extract_rules_from_tree(tree, feature_names, max_rules=10):
    """Extract decision rules from trained tree"""
    tree_ = tree.tree_
    feature_name = [feature_names[i] if i != -2 else "undefined" for i in tree_.feature]
    
    rules = []
    
    def recurse(node, conditions):
        if tree_.feature[node] != -2:
            name = feature_name[node]
            threshold = tree_.threshold[node]
            
            left_conditions = conditions + [(name, "<=", threshold)]
            recurse(tree_.children_left[node], left_conditions)
            
            right_conditions = conditions + [(name, ">", threshold)]
            recurse(tree_.children_right[node], right_conditions)
        else:
            fraud_samples = tree_.value[node][0][1]
            total_samples = tree_.value[node][0].sum()
            
            if fraud_samples > 0 and total_samples > 5:
                confidence = fraud_samples / total_samples
                if confidence > 0.7:
                    rules.append({
                        'conditions': conditions,
                        'confidence': confidence,
                        'support': int(total_samples)
                    })
    
    recurse(0, [])
    rules.sort(key=lambda x: (x['confidence'], x['support']), reverse=True)
    return rules[:max_rules]

def format_drools_rule(rule_id, conditions, confidence, support):
    """Format rule as Drools code with realistic explanations"""
    drools_conditions = []
    risk_factors = []
    explanations = []
    
    for feat, op, val in conditions:
        drools_conditions.append(f"{feat} {op} {val:.2f}")
        
        if feat == 'amount':
            explanations.append(f"Transaction amount {'>=' if op == '>' else '<='} ${val:.2f} - {'High-value' if val > 1000 else 'Unusual amount'} pattern")
            risk_factors.append(f"Amount ${val:.0f}")
        elif feat == 'customerRiskScore':
            explanations.append(f"Customer risk score {'>=' if op == '>' else '<='} {val:.1f}/100 - {'High' if val > 50 else 'Elevated'} risk customer")
            risk_factors.append("High customer risk")
        elif feat == 'riskScore':
            explanations.append(f"Transaction risk score {'>=' if op == '>' else '<='} {val:.1f}/100 - Suspicious pattern")
            risk_factors.append("High transaction risk")
        elif feat == 'transactionVelocity':
            explanations.append(f"Transaction velocity {'>=' if op == '>' else '<='} {val:.0f} txns/hour - {'Rapid' if val > 10 else 'Unusual'} activity")
            risk_factors.append("Velocity anomaly")
        elif feat == 'hourOfDay':
            explanations.append(f"Transaction at hour {int(val)} - {'Off-hours' if val < 6 or val > 22 else 'Peak hours'} activity")
            risk_factors.append("Time-based risk")
        elif feat == 'geographicRiskScore':
            explanations.append(f"Geographic risk {'>=' if op == '>' else '<='} {val:.1f}/10 - {'High-risk' if val > 5 else 'Moderate'} location")
            risk_factors.append("Location risk")
        elif 'Fee' in feat:
            explanations.append(f"{feat} {'>=' if op == '>' else '<='} ${val:.2f} - Abnormal fee structure")
            risk_factors.append("Fee anomaly")
    
    conditions_str = ' , '.join(drools_conditions)
    risk_summary = ', '.join(list(set(risk_factors))[:3]) if risk_factors else 'Multiple fraud indicators'
    explanation_text = '\\n    //     '.join(explanations[:5]) if explanations else 'Pattern learned from fraud cases'
    
    return f"""import net.com.fms_core.dto.message.IsoMessageDTO;

rule "{rule_id}"
    // ============================================================================
    // ML-GENERATED FRAUD RULE - PRODUCTION READY
    // ============================================================================
    //
    // RULE IDENTIFICATION:
    //   Rule ID: {rule_id}
    //   Confidence: {confidence:.2%}
    //   Support: {support} cases
    //   Generated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
    //
    // RISK ASSESSMENT:
    //   Fraud Probability: {confidence * 100:.1f}%
    //   Risk Indicators: {risk_summary}
    //   Action: BLOCK & REVIEW
    //
    // PATTERN ANALYSIS:
    //     {explanation_text}
    //
    // ============================================================================
    
    salience {int(confidence * 100)}
    
when
    $t : IsoMessageDTO({conditions_str})
then
    $t.setRiskLevel("HIGH");
    $t.setFlaggedForReview(true);
    $t.setRuleFired(true);
    $t.setRuleName("{rule_id}");
    $t.setBlockReason("ML Fraud Detection: {rule_id} | Confidence: {confidence:.1%} | {risk_summary}");
    $t.setRiskScore({confidence * 10:.1f});
    $t.setFraudPercentage({confidence * 100:.1f});
    System.out.println("[FRAUD ALERT] {rule_id} triggered - High confidence fraud detection");
end
"""

def extract_rules_from_tree(tree, feature_names, max_rules=10):
    """Extract decision rules from trained tree"""
    tree_ = tree.tree_
    feature_name = [feature_names[i] if i != -2 else "undefined" for i in tree_.feature]
    
    rules = []
    
    def recurse(node, conditions):
        if tree_.feature[node] != -2:
            name = feature_name[node]
            threshold = tree_.threshold[node]
            
            left_conditions = conditions + [(name, "<=", threshold)]
            recurse(tree_.children_left[node], left_conditions)
            
            right_conditions = conditions + [(name, ">", threshold)]
            recurse(tree_.children_right[node], right_conditions)
        else:
            fraud_samples = tree_.value[node][0][1]
            total_samples = tree_.value[node][0].sum()
            
            if fraud_samples > 0 and total_samples > 5:
                confidence = fraud_samples / total_samples
                if confidence > 0.7:
                    rules.append({
                        'conditions': conditions,
                        'confidence': confidence,
                        'support': int(total_samples),
                        'fraud_cases': int(fraud_samples)
                    })
    
    recurse(0, [])
    rules.sort(key=lambda x: (x['confidence'], x['support']), reverse=True)
    return rules[:max_rules]

def format_rule_output(rules, metadata):
    """Format rules as Drools code matching previous format"""
    drools_rules = []
    rule_details = []
    
    for idx, rule in enumerate(rules, 1):
        rule_id = f"ML_Fraud_Pattern_{idx}"
        drools_code = format_drools_rule(
            rule_id, 
            rule['conditions'], 
            rule['confidence'],
            rule['support']
        )
        drools_rules.append(drools_code)
        
        # Extract details for metadata
        risk_factors = []
        for feat, op, val in rule['conditions']:
            if 'customerRiskScore' in feat or 'riskScore' in feat:
                risk_factors.append("High risk score")
            elif 'Velocity' in feat:
                risk_factors.append("Velocity anomaly")
            elif 'geographic' in feat:
                risk_factors.append("Location risk")
            elif 'hourOfDay' in feat:
                risk_factors.append("Time-based risk")
        
        rule_details.append({
            'id': rule_id,
            'rule_name': rule_id,
            'confidence': rule['confidence'],
            'support': rule['support'],
            'depth': len(rule['conditions']),
            'samples': rule['support'],
            'risk_indicators': list(set(risk_factors))[:3],
            'conditions': [{'feature': f, 'operator': o, 'threshold': float(v)} 
                          for f, o, v in rule['conditions']]
        })
    
    output = []
    output.append("===RULES_START===")
    for drools_code in drools_rules:
        output.append(drools_code)
    output.append("===RULES_END===")
    
    # Metadata
    output.append("===METADATA_START===")
    metadata_json = {
        "total_rules": len(rules),
        "model_trained_at": metadata['trained_at'],
        "model_f1_score": metadata['f1_score'],
        "model_precision": metadata['precision'],
        "model_recall": metadata['recall'],
        "generation_method": "pre_trained_model",
        "generated_at": datetime.now().isoformat(),
        "feature_count": len(metadata['feature_names']),
        "rule_details": rule_details
    }
    output.append(json.dumps(metadata_json, indent=2))
    output.append("===METADATA_END===")
    
    return "\n".join(output)

def main():
    try:
        print("Loading pre-trained model...")
        model, scaler, metadata = load_model()
        
        print("Extracting rules from model...")
        rules = extract_rules_from_tree(model, metadata['feature_names'], max_rules=10)
        
        print("Formatting output...")
        result = format_rule_output(rules, metadata)
        
        print(result)
        
    except Exception as e:
        print(f"ERROR: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    main()

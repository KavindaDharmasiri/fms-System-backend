"""
Improved AI Rule Generator for Real-World Fraud Detection
Creates balanced rules that allow legitimate transactions to pass
"""

import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
import sys
import json
from datetime import datetime

class RealisticRuleGenerator:
    """Generate realistic fraud rules that don't flag every transaction"""
    
    def __init__(self):
        self.fraud_patterns = {
            'high_amount': {'threshold': 10000, 'risk': 'HIGH', 'probability': 85},
            'very_high_amount': {'threshold': 50000, 'risk': 'HIGH', 'probability': 95},
            'unusual_time': {'hours': [0, 1, 2, 3, 4, 5], 'risk': 'MID', 'probability': 60},
            'rapid_transactions': {'time_diff': 5, 'risk': 'HIGH', 'probability': 80},
            'round_amounts': {'modulo': 1000, 'min_amount': 5000, 'risk': 'MID', 'probability': 65},
            'high_fees': {'fee_ratio': 0.05, 'risk': 'MID', 'probability': 70},
            'foreign_country': {'risk_countries': ['XX', 'YY'], 'risk': 'MID', 'probability': 55},
            'suspicious_mcc': {'high_risk_mcc': ['5999', '7995', '6012'], 'risk': 'HIGH', 'probability': 75}
        }
    
    def analyze_transaction_data(self, df):
        """Analyze transaction patterns to create realistic thresholds"""
        analysis = {}
        
        if 'amount' in df.columns:
            analysis['amount_stats'] = {
                'mean': df['amount'].mean(),
                'median': df['amount'].median(),
                'p90': df['amount'].quantile(0.90),
                'p95': df['amount'].quantile(0.95),
                'p99': df['amount'].quantile(0.99),
                'max': df['amount'].max()
            }
        
        if 'transactionFeeAmount' in df.columns:
            df['fee_ratio'] = df['transactionFeeAmount'] / df['amount']
            analysis['fee_stats'] = {
                'mean_ratio': df['fee_ratio'].mean(),
                'p90_ratio': df['fee_ratio'].quantile(0.90),
                'p95_ratio': df['fee_ratio'].quantile(0.95)
            }
        
        return analysis
    
    def generate_balanced_rules(self, df):
        """Generate minimal rules that only flag obvious fraud"""
        rules = []
        analysis = self.analyze_transaction_data(df)
        
        # Only create rules for extreme cases
        if 'amount' in df.columns:
            max_amount = df['amount'].max()
            
            # Only flag amounts above ₹10 lakhs (1,000,000)
            if max_amount > 1000000:
                rules.append({
                    'id': 'AI_Extreme_Amount',
                    'conditions': [('amount', '>', 1000000)],
                    'risk_level': 'HIGH',
                    'probability': 80,
                    'description': 'Transaction above ₹10 lakhs'
                })
            
            # Only flag amounts above ₹50 lakhs (5,000,000) as critical
            if max_amount > 5000000:
                rules.append({
                    'id': 'AI_Critical_Amount',
                    'conditions': [('amount', '>', 5000000)],
                    'risk_level': 'HIGH',
                    'probability': 90,
                    'description': 'Critical transaction above ₹50 lakhs'
                })
        
        # Only flag confirmed fraud response codes
        if 'responseCode' in df.columns:
            fraud_codes = ['59']  # Only confirmed fraud code
            existing_codes = df['responseCode'].unique()
            
            for code in fraud_codes:
                if code in existing_codes:
                    rules.append({
                        'id': f'AI_Confirmed_Fraud_{code}',
                        'conditions': [('responseCode', '==', code)],
                        'risk_level': 'HIGH',
                        'probability': 95,
                        'description': f'Confirmed fraud response code {code}'
                    })
        
        # Only flag high-risk merchants with very large amounts
        if 'merchantCategoryCode' in df.columns and 'amount' in df.columns:
            high_risk_mcc = ['7995']  # Only gambling
            for mcc in high_risk_mcc:
                if mcc in df['merchantCategoryCode'].unique():
                    rules.append({
                        'id': f'AI_HighRisk_MCC_{mcc}',
                        'conditions': [
                            ('merchantCategoryCode', '==', mcc),
                            ('amount', '>', 500000)  # Only above ₹5 lakhs
                        ],
                        'risk_level': 'MID',
                        'probability': 60,
                        'description': f'High-risk merchant {mcc} with large amount'
                    })
        
        print(f"Generated {len(rules)} minimal fraud rules (normal transactions will pass)")
        return rules
    
    def format_drools_rule(self, rule):
        """Format rule as Drools with proper risk levels"""
        conditions_str = ' , '.join([
            f"{feat} {op} {val}" if op in ['>', '<', '>=', '<='] 
            else f"{feat} == \"{val}\"" if isinstance(val, str) 
            else f"{feat} == {val}"
            for feat, op, val in rule['conditions']
        ])
        
        return f'''import net.com.fms_core.dto.message.IsoMessageDTO;

rule "{rule['id']}"
    // AI-Generated Fraud Detection Rule
    // Description: {rule['description']}
    // Risk Level: {rule['risk_level']}
    // Fraud Probability: {rule['probability']}%
    // Generated: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
    
    salience {rule['probability']}
    
when
    $t : IsoMessageDTO({conditions_str})
    
then
    // Set appropriate risk level
    $t.setRiskLevel("{rule['risk_level']}");
    
    // Set fraud probability
    $t.setFraudPercentage({rule['probability']}.0);
    
    // Set risk score (0-10 scale)
    $t.setRiskScore({rule['probability'] / 10.0});
    
    // Flag for review only if HIGH risk
    {"$t.setFlaggedForReview(true);" if rule['risk_level'] == 'HIGH' else "// MID risk - no immediate flag"}
    
    // Mark rule as fired
    $t.setRuleFired(true);
    $t.setRuleName("{rule['id']}");
    
    // Set detailed reason
    $t.setBlockReason("AI Rule: {rule['description']} (Risk: {rule['risk_level']}, Probability: {rule['probability']}%)");
    
    System.out.println("[AI RULE] {rule['id']} fired - Risk: {rule['risk_level']}, Probability: {rule['probability']}%");
    
end

'''

def main():
    print("=== REALISTIC AI RULE GENERATOR ===")
    
    # Load transaction data
    csv_file = sys.argv[1] if len(sys.argv) > 1 else "transaction_history.csv"
    
    try:
        df = pd.read_csv(csv_file)
        print(f"Loaded {len(df)} transactions")
        
        # Clean data
        df = df[df['amount'] > 0]  # Remove invalid amounts
        print(f"Valid transactions: {len(df)}")
        
        if len(df) < 10:
            print("ERROR: Insufficient transaction data")
            return
        
        # Generate realistic rules
        generator = RealisticRuleGenerator()
        rules = generator.generate_balanced_rules(df)
        
        print(f"\nGenerated {len(rules)} balanced rules:")
        for rule in rules:
            print(f"  - {rule['id']}: {rule['risk_level']} risk ({rule['probability']}%)")
        
        # Output rules
        print("\n===RULES_START===")
        for rule in rules:
            print(generator.format_drools_rule(rule))
        print("===RULES_END===")
        
        # Output metadata
        metadata = {
            'generated_at': datetime.now().isoformat(),
            'total_rules': len(rules),
            'rule_distribution': {
                'HIGH': len([r for r in rules if r['risk_level'] == 'HIGH']),
                'MID': len([r for r in rules if r['risk_level'] == 'MID']),
                'LOW': len([r for r in rules if r['risk_level'] == 'LOW'])
            },
            'rules': rules
        }
        
        print("\n===METADATA_START===")
        print(json.dumps(metadata, indent=2))
        print("===METADATA_END===")
        
    except Exception as e:
        print(f"Error: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()
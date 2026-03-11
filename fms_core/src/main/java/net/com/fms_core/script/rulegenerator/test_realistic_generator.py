"""
Test script for realistic rule generator
"""

import sys
import os

# Add the script directory to path
script_dir = os.path.dirname(os.path.abspath(__file__))
sys.path.append(script_dir)

from realistic_rule_generator import RealisticRuleGenerator, main
import pandas as pd

def test_realistic_rules():
    print("=== TESTING REALISTIC RULE GENERATOR ===")
    
    # Create test data
    test_data = {
        'amount': [100, 500, 1000, 5000, 15000, 50000, 100000],
        'transactionFeeAmount': [2, 10, 20, 100, 300, 1000, 2000],
        'processingCode': [0, 0, 1, 1, 2, 2, 3],
        'responseCode': ['00', '00', '00', '04', '05', '59', '63'],
        'fired_rule_name': ['NO_RULE', 'NO_RULE', 'NO_RULE', 'fraud_detected', 'fraud_detected', 'fraud_detected', 'fraud_detected']
    }
    
    df = pd.DataFrame(test_data)
    
    generator = RealisticRuleGenerator()
    rules = generator.generate_balanced_rules(df)
    
    print(f"Generated {len(rules)} rules:")
    for rule in rules:
        print(f"  - {rule['id']}: {rule['risk_level']} ({rule['probability']}%)")
        print(f"    Conditions: {rule['conditions']}")
        print(f"    Description: {rule['description']}")
        print()
    
    # Test rule formatting
    if rules:
        print("Sample Drools rule:")
        print(generator.format_drools_rule(rules[0]))

if __name__ == "__main__":
    test_realistic_rules()
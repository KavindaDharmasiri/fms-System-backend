#!/usr/bin/env python3
"""
Simple test script to verify Python integration with Java
"""

import json
import sys
from datetime import datetime
from impossible_distance_model import ImpossibleDistanceModel, Transaction

def main():
    try:
        # Initialize the model
        model = ImpossibleDistanceModel()
        
        # Create sample transactions
        current_transaction = Transaction(
            latitude=40.7128,  # New York
            longitude=-74.0060,
            timestamp=datetime.now(),
            card_number='1234****',
            amount=500
        )
        
        historical_transactions = [
            Transaction(
                latitude=51.5074,  # London
                longitude=-0.1278,
                timestamp=datetime.now(),
                card_number='1234****',
                amount=200
            )
        ]
        
        # Run analysis
        results = model.comprehensive_fraud_analysis(current_transaction, historical_transactions)
        
        # Export results for Java
        with open('fraud_analysis_results.json', 'w') as f:
            json.dump(results, f, indent=2, default=str)
        
        print("Python fraud analysis completed successfully!")
        return 0
        
    except Exception as e:
        print(f"Error in Python analysis: {e}")
        return 1

if __name__ == "__main__":
    sys.exit(main())
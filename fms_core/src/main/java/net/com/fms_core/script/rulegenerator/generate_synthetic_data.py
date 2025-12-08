# Generate large synthetic transaction dataset for training
# Run this in Google Colab

import pandas as pd
import numpy as np

np.random.seed(42)

# Number of transactions to generate
n_samples = 10000

print(f"Generating {n_samples} synthetic transactions...")

# Generate features
amounts = np.random.uniform(10, 10000, n_samples)
transaction_fees = amounts * np.random.uniform(0.01, 0.05, n_samples)
settlement_fees = amounts * np.random.uniform(0.005, 0.02, n_samples)
transaction_processing_fees = np.random.uniform(0.5, 5, n_samples)
settlement_processing_fees = np.random.uniform(0.3, 3, n_samples)

# Define rules based on patterns
rules = []
for i in range(n_samples):
    amt = amounts[i]
    txn_fee = transaction_fees[i]
    
    if amt > 5000:
        rules.append("high_amount_transaction")
    elif amt > 2000 and txn_fee > 50:
        rules.append("medium_high_risk")
    elif amt > 1000:
        rules.append("medium_amount_transaction")
    elif amt < 100 and txn_fee < 2:
        rules.append("micro_transaction")
    elif txn_fee > amt * 0.04:
        rules.append("high_fee_transaction")
    elif settlement_fees[i] > 20:
        rules.append("high_settlement_cost")
    elif amt > 500 and amt <= 1000:
        rules.append("standard_transaction")
    else:
        rules.append("low_risk_transaction")

# Create DataFrame
df = pd.DataFrame({
    'amount': amounts,
    'transactionFeeAmount': transaction_fees,
    'settlementFeeAmount': settlement_fees,
    'transactionProcessingFee': transaction_processing_fees,
    'settlementProcessingFee': settlement_processing_fees,
    'fired_rule_name': rules
})

# Add some noise and edge cases
noise_indices = np.random.choice(n_samples, size=int(n_samples * 0.1), replace=False)
df.loc[noise_indices, 'fired_rule_name'] = np.random.choice(
    ['anomaly_detected', 'velocity_check', 'geo_risk', 'first_amount', 'second_amount'],
    size=len(noise_indices)
)

print(f"\nDataset created!")
print(f"Total transactions: {len(df)}")
print(f"Unique rules: {df['fired_rule_name'].nunique()}")
print(f"\nRule distribution:")
print(df['fired_rule_name'].value_counts())

print(f"\nFeature statistics:")
print(df.describe())

# Save to CSV
df.to_csv('synthetic_transaction_history.csv', index=False)
print("\nSaved to 'synthetic_transaction_history.csv'")
print("Use this file to train your model in Colab!")

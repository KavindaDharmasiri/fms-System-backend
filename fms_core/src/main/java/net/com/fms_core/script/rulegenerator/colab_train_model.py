# Run this in Google Colab to pre-train the model
# Upload your transaction_history.csv file to Colab

import pandas as pd
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import LabelEncoder
import joblib

# Load your CSV file (upload to Colab first)
df = pd.read_csv('transaction_history.csv')
df = df.fillna(0)

print(f"Total transactions: {len(df)}")
print(f"Unique rules: {df['fired_rule_name'].nunique()}")
print(f"Rule distribution:\n{df['fired_rule_name'].value_counts()}")

# Prepare features and target
feature_columns = [
    'amount', 'transactionFeeAmount', 'settlementFeeAmount',
    'transactionProcessingFee', 'settlementProcessingFee'
]

X = df[feature_columns]
y = df['fired_rule_name']

# Encode target
y_encoder = LabelEncoder()
y_encoded = y_encoder.fit_transform(y)

print(f"\nTraining model with {len(set(y_encoded))} unique rule types...")

# Train model
clf = DecisionTreeClassifier(max_depth=5, min_samples_split=2, min_samples_leaf=1, random_state=42)
clf.fit(X, y_encoded)

print(f"Model trained! Tree depth: {clf.get_depth()}, Leaves: {clf.get_n_leaves()}")

# Save model
joblib.dump({'model': clf, 'encoder': y_encoder}, 'decision_tree_model.pkl')
print("\nModel saved to 'decision_tree_model.pkl'")
print("Download this file and place it in your rulegenerator folder")

# Visualize tree (optional)
from sklearn.tree import plot_tree
import matplotlib.pyplot as plt

plt.figure(figsize=(20,10))
plot_tree(clf, feature_names=feature_columns, class_names=y_encoder.classes_, filled=True, fontsize=10)
plt.savefig('decision_tree.png', dpi=300, bbox_inches='tight')
print("Tree visualization saved to 'decision_tree.png'")

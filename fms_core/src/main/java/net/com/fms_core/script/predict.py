import sys
import pandas as pd
import joblib

# Load the model
model = joblib.load("impossible_distance_model.pkl")

# Read transaction data from command-line argument (JSON)
import json
transaction_json = sys.argv[1]
data = pd.DataFrame([json.loads(transaction_json)])

# Select same features used in training
features = ['distance_km', 'time_diff_minutes', 'required_speed_kmh']
X = data[features]

# Predict
prediction = model.predict(X)
probability = model.predict_proba(X)[:, 1]

# Output result as JSON
result = {"prediction": int(prediction[0]), "probability": float(probability[0])}
print(json.dumps(result))

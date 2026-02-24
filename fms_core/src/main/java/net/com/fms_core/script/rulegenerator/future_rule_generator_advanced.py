# Enhanced future_rule_generator.py with advanced features

import pandas as pd
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report, accuracy_score
import joblib
import sys

DB_CSV_FILE = sys.argv[1] if len(sys.argv) > 1 else "transaction_history.csv"

def load_and_engineer_features(file_path):
    df = pd.read_csv(file_path)
    df = df.fillna(0)
    
    # ADVANCED: Add velocity features (if timestamp available)
    if 'timestamp' in df.columns:
        df['timestamp'] = pd.to_datetime(df['timestamp'])
        df = df.sort_values('timestamp')
        df['hour_of_day'] = df['timestamp'].dt.hour
        df['day_of_week'] = df['timestamp'].dt.dayofweek
    
    # ADVANCED: Amount-based features
    df['amount_log'] = df['amount'].apply(lambda x: 0 if x <= 0 else pd.np.log(x))
    df['is_high_amount'] = (df['amount'] > df['amount'].quantile(0.9)).astype(int)
    
    # ADVANCED: Fee ratio analysis
    df['fee_ratio'] = df['transactionFeeAmount'] / (df['amount'] + 1)
    
    return df

def preprocess_with_validation(df):
    feature_columns = [
        'amount', 'amount_log', 'is_high_amount',
        'transactionFeeAmount', 'settlementFeeAmount',
        'transactionProcessingFee', 'settlementProcessingFee',
        'fee_ratio'
    ]
    
    # Add time features if available
    if 'hour_of_day' in df.columns:
        feature_columns.extend(['hour_of_day', 'day_of_week'])
    
    X = df[feature_columns]
    y = df['fired_rule_name']
    y_encoder = LabelEncoder()
    y_encoded = y_encoder.fit_transform(y)
    
    # ADVANCED: Split for validation
    X_train, X_test, y_train, y_test = train_test_split(
        X, y_encoded, test_size=0.2, random_state=42, stratify=y_encoded
    )
    
    return X_train, X_test, y_train, y_test, y_encoder, X.columns

def train_and_validate(X_train, X_test, y_train, y_test, y_encoder):
    clf = DecisionTreeClassifier(
        max_depth=5,  # Deeper for complex patterns
        min_samples_split=10,
        min_samples_leaf=5,
        random_state=42
    )
    clf.fit(X_train, y_train)
    
    # ADVANCED: Validation metrics
    y_pred = clf.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)
    
    print(f"Model Accuracy: {accuracy:.2%}")
    print(f"Tree Depth: {clf.get_depth()}, Leaves: {clf.get_n_leaves()}")
    
    # Feature importance
    feature_importance = sorted(
        zip(X_train.columns, clf.feature_importances_),
        key=lambda x: x[1],
        reverse=True
    )
    print("\nTop Features:")
    for feat, imp in feature_importance[:5]:
        print(f"  {feat}: {imp:.3f}")
    
    return clf, accuracy

def generate_advanced_rules(clf, y_encoder, feature_names, min_confidence=0.7):
    from sklearn.tree import _tree
    tree_ = clf.tree_
    rules = []
    rule_counter = [0]
    
    def get_confidence(node):
        value = tree_.value[node][0]
        total = value.sum()
        max_class = value.max()
        return max_class / total if total > 0 else 0
    
    def recurse(node, conditions, depth=0):
        if tree_.feature[node] != _tree.TREE_UNDEFINED:
            name = feature_names[tree_.feature[node]]
            threshold = tree_.threshold[node]
            
            left_cond = conditions + [f"{name} > {threshold:.2f}"]
            right_cond = conditions + [f"{name} <= {threshold:.2f}"]
            
            recurse(tree_.children_left[node], left_cond, depth + 1)
            recurse(tree_.children_right[node], right_cond, depth + 1)
        else:
            if not conditions:
                return
            
            confidence = get_confidence(node)
            
            # ADVANCED: Only generate high-confidence rules
            if confidence < min_confidence:
                return
            
            value = tree_.value[node][0]
            class_idx = value.argmax()
            rule_name = y_encoder.inverse_transform([class_idx])[0]
            
            conditions_str = ' , '.join(conditions)
            rule_id = f"AI_Advanced_{rule_name}_{rule_counter[0]}"
            rule_counter[0] += 1
            
            drools_rule = f"""import net.com.fms_core.dto.message.IsoMessageDTO;

rule "{rule_id}"
// Confidence: {confidence:.2%}, Depth: {depth}
when
    $t : IsoMessageDTO({conditions_str})
then
    $t.setRiskLevel("HIGH");
    $t.setFlaggedForReview(true);
    $t.setRuleFired(true);
    $t.setRuleName("{rule_name}");
    $t.setFiredRule("AI Generated - Confidence: {confidence:.2%}");
    $t.setRiskScore({confidence * 10:.1f});
end
"""
            rules.append(drools_rule)
    
    recurse(0, [])
    return rules

if __name__ == "__main__":
    print("=== Advanced Rule Generation ===")
    
    # Load with feature engineering
    df = load_and_engineer_features(DB_CSV_FILE)
    print(f"Transactions: {len(df)}, Unique Rules: {df['fired_rule_name'].nunique()}")
    
    # Preprocess with validation split
    X_train, X_test, y_train, y_test, y_encoder, feature_names = preprocess_with_validation(df)
    
    # Train and validate
    clf, accuracy = train_and_validate(X_train, X_test, y_train, y_test, y_encoder)
    
    # Generate rules with confidence threshold
    rules = generate_advanced_rules(clf, y_encoder, feature_names, min_confidence=0.7)
    
    print(f"\nGenerated {len(rules)} high-confidence rules")
    
    # Output for Java
    print("===RULES_START===")
    for rule in rules:
        print(rule)
    print("===RULES_END===")

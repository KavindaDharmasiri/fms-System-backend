"""
Python Model for Impossible Distance Detection in Fraud Management System
Author: FMS Development Team
Purpose: Advanced geospatial analysis for transaction fraud detection
"""

import numpy as np
import json
from datetime import datetime, timedelta
from typing import List, Dict, Tuple, Any
from dataclasses import dataclass
from scipy.cluster.hierarchy import linkage, fcluster
from scipy.spatial.distance import pdist
from sklearn.metrics import silhouette_score
import warnings
warnings.filterwarnings('ignore')

@dataclass
class Transaction:
    latitude: float
    longitude: float
    timestamp: datetime
    card_number: str
    amount: float

class ImpossibleDistanceModel:
    def __init__(self):
        self.max_speeds = {
            'walking': 6,
            'cycling': 25,
            'car_city': 60,
            'car_highway': 130,
            'train_regular': 160,
            'train_high_speed': 320,
            'commercial_flight': 900,
            'supersonic_flight': 2100
        }
        
        # Pre-trained neural network weights
        self.W1 = np.array([
            [0.5, -0.3, 0.8, 0.6, 0.4],
            [-0.2, 0.7, -0.5, 0.3, 0.9],
            [0.4, 0.1, 0.6, -0.4, 0.2]
        ])
        self.b1 = np.array([[0.1], [-0.2], [0.3]])
        self.W2 = np.array([[0.8, -0.6, 0.4]])
        self.b2 = np.array([[0.1]])

    def haversine_distance(self, lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        """Calculate haversine distance between two points"""
        R = 6371  # Earth's radius in kilometers
        
        # Convert to radians
        lat1_rad, lon1_rad = np.radians([lat1, lon1])
        lat2_rad, lon2_rad = np.radians([lat2, lon2])
        
        # Haversine formula
        dlat = lat2_rad - lat1_rad
        dlon = lon2_rad - lon1_rad
        
        a = np.sin(dlat/2)**2 + np.cos(lat1_rad) * np.cos(lat2_rad) * np.sin(dlon/2)**2
        c = 2 * np.arctan2(np.sqrt(a), np.sqrt(1-a))
        
        return R * c

    def analyze_travel_feasibility(self, distance_km: float, time_diff_minutes: float) -> Tuple[int, bool]:
        """Analyze travel feasibility and return risk score"""
        if time_diff_minutes <= 0:
            return 0, False
            
        required_speed_kmh = (distance_km / time_diff_minutes) * 60
        
        # Risk scoring
        if required_speed_kmh <= self.max_speeds['walking']:
            risk_score, transport_mode = 0, 'walking'
        elif required_speed_kmh <= self.max_speeds['cycling']:
            risk_score, transport_mode = 10, 'cycling'
        elif required_speed_kmh <= self.max_speeds['car_city']:
            risk_score, transport_mode = 20, 'car_city'
        elif required_speed_kmh <= self.max_speeds['car_highway']:
            risk_score, transport_mode = 30, 'car_highway'
        elif required_speed_kmh <= self.max_speeds['train_regular']:
            risk_score, transport_mode = 40, 'train_regular'
        elif required_speed_kmh <= self.max_speeds['train_high_speed']:
            risk_score, transport_mode = 60, 'train_high_speed'
        elif required_speed_kmh <= self.max_speeds['commercial_flight']:
            risk_score, transport_mode = 80, 'commercial_flight'
        elif required_speed_kmh <= self.max_speeds['supersonic_flight']:
            risk_score, transport_mode = 95, 'supersonic_flight'
        else:
            risk_score, transport_mode = 100, 'impossible'
        
        # Check if impossible (with 20% buffer)
        max_feasible_speed = self.max_speeds['commercial_flight'] * 1.2
        is_impossible = required_speed_kmh > max_feasible_speed
        
        print(f"Distance: {distance_km:.2f} km, Time: {time_diff_minutes:.0f} min, Required Speed: {required_speed_kmh:.2f} km/h")
        print(f"Transport Mode: {transport_mode}, Risk Score: {risk_score}, Impossible: {is_impossible}")
        
        return risk_score, is_impossible

    def analyze_location_clusters(self, transactions: List[Transaction]) -> int:
        """Analyze transaction location patterns"""
        if len(transactions) < 2:
            return 0
        
        # Extract coordinates
        coords = np.array([[txn.latitude, txn.longitude] for txn in transactions])
        
        # Calculate pairwise distances using haversine
        distances = pdist(coords, lambda u, v: self.haversine_distance(u[0], u[1], v[0], v[1]))
        
        if len(distances) == 0:
            return 0
        
        # Perform hierarchical clustering
        try:
            linkage_matrix = linkage(distances, method='ward')
            
            # Simple clustering with 2-3 clusters
            max_clusters = min(3, len(transactions) - 1)
            if max_clusters < 2:
                return 0
                
            cluster_labels = fcluster(linkage_matrix, max_clusters, criterion='maxclust')
            
            # Calculate maximum distance within clusters
            max_cluster_distance = 0
            for cluster_id in np.unique(cluster_labels):
                cluster_coords = coords[cluster_labels == cluster_id]
                if len(cluster_coords) > 1:
                    cluster_distances = pdist(cluster_coords, 
                                            lambda u, v: self.haversine_distance(u[0], u[1], v[0], v[1]))
                    if len(cluster_distances) > 0:
                        max_cluster_distance = max(max_cluster_distance, np.max(cluster_distances))
            
            # Risk scoring based on geographical spread
            if max_cluster_distance > 10000:
                return 90
            elif max_cluster_distance > 5000:
                return 70
            elif max_cluster_distance > 1000:
                return 50
            elif max_cluster_distance > 100:
                return 30
            else:
                return 10
                
        except Exception:
            return 0

    def analyze_temporal_patterns(self, transactions: List[Transaction]) -> int:
        """Analyze time-based movement patterns"""
        if len(transactions) < 2:
            return 0
        
        # Sort by timestamp
        sorted_txns = sorted(transactions, key=lambda x: x.timestamp)
        
        velocities = []
        for i in range(1, len(sorted_txns)):
            time_diff_hours = (sorted_txns[i].timestamp - sorted_txns[i-1].timestamp).total_seconds() / 3600
            
            if time_diff_hours > 0:
                distance = self.haversine_distance(
                    sorted_txns[i-1].latitude, sorted_txns[i-1].longitude,
                    sorted_txns[i].latitude, sorted_txns[i].longitude
                )
                velocity = distance / time_diff_hours
                velocities.append(velocity)
        
        if not velocities:
            return 0
        
        mean_velocity = np.mean(velocities)
        max_velocity = np.max(velocities)
        velocity_std = np.std(velocities)
        
        # Risk scoring
        if max_velocity > 1000:
            return 95
        elif max_velocity > 500:
            return 70
        elif mean_velocity > 200:
            return 50
        elif velocity_std > 100:
            return 40
        else:
            return 20

    def ml_fraud_prediction(self, features: np.ndarray) -> float:
        """Simple neural network for fraud prediction"""
        # Normalize features
        if np.std(features) > 0:
            normalized_features = (features - np.mean(features)) / np.std(features)
        else:
            normalized_features = features
        
        # Forward propagation
        z1 = self.W1 @ normalized_features.reshape(-1, 1) + self.b1
        a1 = 1 / (1 + np.exp(-z1))  # Sigmoid
        
        z2 = self.W2 @ a1 + self.b2
        ml_risk_score = (1 / (1 + np.exp(-z2))) * 100
        
        return float(ml_risk_score[0, 0])

    def comprehensive_fraud_analysis(self, current_txn: Transaction, 
                                   historical_txns: List[Transaction]) -> Dict[str, Any]:
        """Main analysis pipeline"""
        print("\n=== COMPREHENSIVE FRAUD ANALYSIS ===")
        
        # Initialize risks
        distance_risk = 0
        is_impossible = False
        cluster_risk = 0
        temporal_risk = 0
        ml_risk = 0
        
        # Distance Analysis
        if historical_txns:
            last_txn = historical_txns[-1]
            distance = self.haversine_distance(
                last_txn.latitude, last_txn.longitude,
                current_txn.latitude, current_txn.longitude
            )
            time_diff_minutes = (current_txn.timestamp - last_txn.timestamp).total_seconds() / 60
            distance_risk, is_impossible = self.analyze_travel_feasibility(distance, time_diff_minutes)
        
        # Clustering Analysis
        if historical_txns:
            all_txns = historical_txns + [current_txn]
            cluster_risk = self.analyze_location_clusters(all_txns)
        
        # Temporal Pattern Analysis
        if historical_txns:
            all_txns = historical_txns + [current_txn]
            temporal_risk = self.analyze_temporal_patterns(all_txns)
        
        # Machine Learning Prediction
        if historical_txns:
            required_speed = (distance / time_diff_minutes) * 60 if time_diff_minutes > 0 else 0
            features = np.array([distance, time_diff_minutes, required_speed, cluster_risk, temporal_risk])
            ml_risk = self.ml_fraud_prediction(features)
        
        # Weighted Risk Calculation
        weights = [0.4, 0.2, 0.2, 0.2]  # [distance, cluster, temporal, ml]
        final_risk_score = (weights[0] * distance_risk + 
                          weights[1] * cluster_risk + 
                          weights[2] * temporal_risk + 
                          weights[3] * ml_risk)
        
        # Fraud Probability
        fraud_probability = 1 / (1 + np.exp(-(final_risk_score - 50) / 10))
        
        # Generate Recommendations
        if final_risk_score >= 80:
            recommendations = ['BLOCK_TRANSACTION', 'IMMEDIATE_ALERT', 'FREEZE_CARD']
        elif final_risk_score >= 60:
            recommendations = ['FLAG_FOR_REVIEW', 'ADDITIONAL_VERIFICATION', 'MONITOR_CLOSELY']
        elif final_risk_score >= 40:
            recommendations = ['ENHANCED_MONITORING', 'LOG_SUSPICIOUS_ACTIVITY']
        else:
            recommendations = ['ALLOW_TRANSACTION', 'ROUTINE_MONITORING']
        
        # Display Results
        print(f"Distance Risk: {distance_risk:.1f}")
        print(f"Cluster Risk: {cluster_risk:.1f}")
        print(f"Temporal Risk: {temporal_risk:.1f}")
        print(f"ML Risk: {ml_risk:.1f}")
        print(f"Final Risk Score: {final_risk_score:.1f}")
        print(f"Fraud Probability: {fraud_probability * 100:.2f}%")
        print(f"Recommendations: {', '.join(recommendations)}")
        
        return {
            'risk_score': final_risk_score,
            'fraud_probability': fraud_probability,
            'recommendations': recommendations,
            'distance_risk': distance_risk,
            'cluster_risk': cluster_risk,
            'temporal_risk': temporal_risk,
            'ml_risk': ml_risk,
            'is_impossible': is_impossible
        }

def main():
    """Example usage"""
    model = ImpossibleDistanceModel()
    
    # Sample data
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
            timestamp=datetime.now() - timedelta(hours=2),
            card_number='1234****',
            amount=200
        ),
        Transaction(
            latitude=35.6762,  # Tokyo
            longitude=139.6503,
            timestamp=datetime.now() - timedelta(hours=6),
            card_number='1234****',
            amount=300
        )
    ]
    
    # Run analysis
    results = model.comprehensive_fraud_analysis(current_transaction, historical_transactions)
    
    # Export results
    with open('fraud_analysis_results.json', 'w') as f:
        json.dump({k: v for k, v in results.items() if k != 'recommendations'}, f, indent=2, default=str)
        
    print("\nResults exported to fraud_analysis_results.json")
    print("Model analysis complete!")

if __name__ == "__main__":
    main()
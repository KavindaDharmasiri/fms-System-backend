import csv
import random

# File to save
filename = "transactions.csv"

# Number of rows to generate
num_rows = 500

# Max values for realistic ranges
MAX_DISTANCE_KM = 1000      # Max distance between transactions
MAX_TIME_MINUTES = 1440     # 24 hours in minutes

# Open CSV file
with open(filename, mode='w', newline='') as file:
    writer = csv.writer(file)
    # Write header
    writer.writerow(["distance_km", "time_diff_minutes", "required_speed_kmh", "impossible_label"])

    for _ in range(num_rows):
        # Randomly generate distance (1 km to MAX_DISTANCE_KM)
        distance = round(random.uniform(0.1, MAX_DISTANCE_KM), 2)
        # Randomly generate time difference (1 min to MAX_TIME_MINUTES)
        time_diff = round(random.uniform(1, MAX_TIME_MINUTES), 2)
        # Calculate required speed
        required_speed = round((distance / time_diff) * 60, 2)

        # Decide if impossible
        # Assume speeds over 1000 km/h are impossible for this simulation
        impossible = 1 if required_speed > 1000 else 0

        writer.writerow([distance, time_diff, required_speed, impossible])

print(f"Generated {num_rows} rows in {filename}")

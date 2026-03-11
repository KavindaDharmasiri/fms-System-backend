"""
Test script to verify Python environment for dynamic rule generation
"""

import sys
print(f"Python version: {sys.version}")

try:
    import pandas as pd
    print(f"✓ pandas version: {pd.__version__}")
except ImportError as e:
    print(f"✗ pandas not found: {e}")

try:
    import numpy as np
    print(f"✓ numpy version: {np.__version__}")
except ImportError as e:
    print(f"✗ numpy not found: {e}")

try:
    from sklearn.ensemble import RandomForestClassifier
    import sklearn
    print(f"✓ scikit-learn version: {sklearn.__version__}")
except ImportError as e:
    print(f"✗ scikit-learn not found: {e}")

try:
    import json
    print("✓ json module available")
except ImportError as e:
    print(f"✗ json not found: {e}")

try:
    import os
    print("✓ os module available")
except ImportError as e:
    print(f"✗ os not found: {e}")

print("\n=== ENVIRONMENT TEST COMPLETE ===")
print("If all modules show ✓, the environment is ready for dynamic rule generation.")
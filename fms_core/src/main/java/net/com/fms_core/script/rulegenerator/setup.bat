@echo off
echo Setting up ML Fraud Detection Environment...
echo.

echo Installing Python dependencies...
pip install -r requirements.txt

echo.
echo Testing Python environment...
python -c "import pandas, numpy, sklearn; print('All dependencies installed successfully!')"

echo.
echo Testing ML model trainer...
python ml_fraud_model_trainer.py

echo.
echo Setup complete! You can now:
echo 1. Train models using the 'Train Model' button in the UI
echo 2. Generate rules using the 'Generate Future Rules' button
echo.
pause
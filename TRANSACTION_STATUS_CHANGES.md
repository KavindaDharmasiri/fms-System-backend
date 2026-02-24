# Transaction Status Enhancement

## Problem
The impossible distance check was only comparing against the **last transaction**, not considering whether it was blocked. This caused false positives when legitimate travel occurred after a blocked transaction.

## Solution

### 1. Database Changes
- Added `action_status` column to `transaction_history` table
- Values: `ALLOWED`, `BLOCKED`, `REVIEW`
- Existing `status` field continues to store risk level (HIGH/MID/LOW)

### 2. Logic Changes

#### ImpossibleDistanceServiceImpl
- Now loops through recent transactions to find the **last non-blocked transaction**
- Compares current transaction against the last **ALLOWED** or **REVIEW** transaction
- Skips transactions with `action_status = 'BLOCKED'`

#### DynamicDroolsServiceImpl
- Reads `ReactionTemplate.status` for each fired rule
- Sets `action_status` based on the most restrictive reaction template:
  - `BLOCKED` > `REVIEW` > `ALLOWED`
- Saves `action_status` to transaction history

### 3. Flow Example

**Scenario:**
1. Transaction 1: Osaka → Galle (impossible distance)
   - Rule fires → Reaction Template status = "BLOCKED"
   - Saved as: `status=HIGH`, `action_status=BLOCKED`

2. Transaction 2: Galle (same location)
   - Compares against Transaction 1
   - Transaction 1 is BLOCKED, so it's skipped
   - No previous valid transaction found → ALLOWED

3. Transaction 3: Galle → Tokyo (after 5 hours)
   - Compares against Transaction 2 (last ALLOWED)
   - Calculates distance from Galle to Tokyo
   - If time is sufficient → ALLOWED

## Benefits
- Accurate fraud detection based on legitimate travel history
- Prevents false positives from blocked transactions
- Maintains audit trail with both risk level and action taken

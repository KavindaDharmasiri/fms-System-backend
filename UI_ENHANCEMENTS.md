# High-Risk Transaction View Enhancements

## Backend Changes

### 1. Database Schema
Added columns to `transaction_history` table:
- `action_status` - Stores the action from reaction template (BLOCKED/REVIEW/ALLOWED)
- `reaction_template_name` - Name of the applied reaction template
- `sms_enabled`, `email_enabled`, `frm_enabled` - Reaction template actions
- `manual_review_status` - Manual status override (APPROVED/REJECTED/UNDER_REVIEW/FALSE_POSITIVE)
- `manual_review_reason` - Reason for manual status change
- `reviewed_by` - User who reviewed the transaction
- `reviewed_at` - Timestamp of review

### 2. New API Endpoint
**PUT** `/api/v1/tran/update-status`
- Updates transaction status manually
- Records reviewer and reason
- Returns success/error response

### 3. Updated DTOs
- `TransactionHistoryDTO` - Added all new fields
- `TransactionStatusUpdateDTO` - New DTO for status updates

## Frontend Changes

### 1. Transaction Details View
Added new sections to display:
- **Action Status Badge** - Visual indicator (BLOCKED/REVIEW/ALLOWED)
- **Reaction Template Actions** - Shows SMS, Email, FRM enabled status
- **Manual Review Section** - Shows review status, reviewer, timestamp, and reason

### 2. Status Change Modal
- Dropdown to select new status (APPROVED/REJECTED/UNDER_REVIEW/FALSE_POSITIVE)
- Textarea for reason
- Submit button with loading state
- Calls backend API to update status

### 3. Component Updates
- Added modal state management
- HTTP call to update status
- Reload data after successful update
- Error handling with alerts

## Usage Flow

1. User views high-risk transaction details
2. Sees all triggered rules and reaction template actions
3. Clicks "Change Status" button
4. Selects new status and provides reason
5. System updates backend and shows confirmation
6. Transaction history is updated with reviewer info

## Benefits
- Complete audit trail of all actions
- Manual override capability for false positives
- Clear visibility of automated vs manual decisions
- Reaction template actions are preserved for compliance

# comp5590-gp-booking-system


## Overview
Group 13B Implementation for part of a General Practitioner (GP) Booking System. This Project is focused on the Implementation for the Doctor's Interface for the System.


## Tech Stack
Java is the main language used for both frontend and backend, utilising the Java Swing library for the UI design. For the RDBMS, MySQL is used.


## Roadmap
- Login to the system as a role and logout.

- Log all access to the system, e.g. when Doctor X views Patient Y’s information.

- View bookings.

- View own patients.

- Enter visit details and prescriptions (and send confirmation messages).

- View visit details and prescriptions.

- Edit visit details and prescriptions (and send confirmation messages).

- Assign new doctor to a patient (and send confirmation messages).

- View all patients.

- Send message to an admin/receptionist.



## Getting Started

### Git Config: In the Command Line
git config --global user.name "Your Name"

git config --global user.email xyz123@kent.ac.uk"


### Open your IDE of Choice
### For SSH: In the Command Line/Terminal
git clone git@git.cs.kent.ac.uk:jo439/comp5590-gp-booking-system.git

cd comp5590-gp-booking-system

### For HTTPS: In the Command Line/Terminal
git clone https://git.cs.kent.ac.uk/jo439/comp5590-gp-booking-system.git

cd comp5590-gp-booking-system



## Workflow
### Creating a Branch
git checkout -b feature-branch

### Checking your Branch
git branch

### Staging Changes a Single File Change
git add your-file

### Staging All File Changes
git add .

### Commit Changes
git commit -m "xyz123: Description of Implementation"

### Pulling Latest Changes to Main
git checkout main

git pull origin main


### Push Changes to GitLab Repository
git push origin feature-branch



## Create Merge Request
1. Open Gitlab

2. Click Merge Requests

3. Click New Merge Request

4. Select source branch (feature-branch) → target branch (main)

5. Add a title and description about your feature

6. Create Merge Request

7. Wait for Review & Approval

8. After Approval Click Merge on GitLab



## Once Feature Merged

### Everyone Must:
1. git checkout main

2. git pull origin main


### If Working on a Feature:
3. git checkout feature-branch

4. git merge main




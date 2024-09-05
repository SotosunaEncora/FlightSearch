# FlightSearch

## Overview

FlightSearch is a full-stack application that allows users to search for flights based on various parameters such as departure and arrival airports, dates, number of adults, currency, and more. The application is built with a React frontend and a Spring Boot backend, using Docker to containerize and run the services.

## Prerequisites

Before running this application, ensure you have the following installed:

- Docker
- Docker Compose
- Amadeus API credentials (API Key and API Secret)

## Getting Started

### Step 1: Clone the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/yourusername/FlightSearch.git
cd FlightSearch
```

### Step 2: Set Up Amadeus API Credentials

You need to obtain credentials to use the Amadeus API. Once you have the API Key and API Secret, follow these steps:

1. Navigate to the `BE/flightsearch-backend` directory.
2. Create a `.env` file in this directory.
3. Add your Amadeus API credentials to the `.env` file in the following format:

```plaintext
API_KEY=your_amadeus_api_key
API_SECRET=your_amadeus_api_secret
API_BASE_URL=amadeus_url
```

### Step 3: Run the Application

To run the full-stack application, use Docker Compose:

```bash
docker-compose up --build
```

This command will build and start both the frontend and backend services.

### Step 4: Access the Application

Once the application is running, you can access it in your web browser at:

```
http://localhost:3000
```

The backend service will be running at:

```
http://localhost:8080
```

## Project Structure

The project is organized into two main directories:

- **FE/flightsearch-frontend**: Contains the frontend React application.
- **BE/flightsearch-backend**: Contains the backend Spring Boot application.

### Frontend

- Built with React and TypeScript.
- Material UI is used for the UI components.
- Axios is used for making HTTP requests to the backend.

### Backend

- Built with Spring Boot and Java.
- Connects to the Amadeus API for fetching flight data.
- Uses `RestTemplate` to handle HTTP requests.

## Usage

After the application is up and running, you can:

- Search for flights by entering departure and arrival airports, dates, number of adults, currency, and other parameters.
- View search results and flight details.
- Use filters to customize your search experience.

## Troubleshooting

If you encounter any issues:

1. Ensure your Amadeus API credentials are correct and have been added to the `.env` file in the backend service.
2. Make sure Docker and Docker Compose are installed and running on your machine.
3. Check the Docker logs for any errors by running:

```bash
docker-compose logs
```

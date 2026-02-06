# Node.js development server for Angular

FROM node:20-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Expose Angular dev server port
EXPOSE 4200

# Start Angular development server
# Using direct ng command to ensure flags are properly passed
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--poll", "2000"]

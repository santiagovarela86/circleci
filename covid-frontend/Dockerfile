FROM node:13.12.0-alpine as builder
WORKDIR /usr/src/app
COPY . ./
RUN npm install --production
RUN npm run build

FROM node:13.12.0-alpine
EXPOSE 3000
RUN apk add --no-cache --upgrade bash
WORKDIR /usr/src/app
COPY --from=builder /usr/src/app ./
RUN npm install -g serve

FROM node:18

WORKDIR /app

COPY ./app /app

RUN npm install axios sweetalert2 react-router-dom jwt-decode

RUN npm install --save-dev @babel/core @babel/preset-env @babel/preset-react babel-jest
RUN npm install --save-dev @babel/plugin-proposal-private-property-in-object
RUN npm install --save-dev @babel/preset-env @babel/preset-react @babel/plugin-proposal-private-methods @babel/plugin-proposal-private-property-in-object @babel/plugin-syntax-class-properties @babel/plugin-proposal-optional-chaining @babel/plugin-proposal-nullish-coalescing-operator
RUN npm install --save-dev @testing-library/react @testing-library/jest-dom @testing-library/user-event
RUN npm install --save-dev jest
RUN npm install --save-dev @types/jest @types/axios
RUN npm install  --save-dev axios-mock-adapter


RUN npm run build

CMD ["npm", "start"]

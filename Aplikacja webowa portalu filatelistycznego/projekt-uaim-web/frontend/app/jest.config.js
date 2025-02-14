module.exports = {
    transform: {
    '^.+\\.(js|jsx|ts|tsx)$': 'babel-jest',   // Transformacja plików JS/JSX przy użyciu Babel
  },
    transformIgnorePatterns: [
      '/node_modules/(?!(axios)/)', // Wyklucz "axios" z ignorowanych
    ],
    // preset: 'react-app',
    // setupFilesAfterEnv: ['@testing-library/jest-dom/extend-expect'],
    // testEnvironment: 'jsdom',
    // collectCoverage: true,  // Włączenie zbierania danych o pokryciu kodu
    // collectCoverageFrom: [
    //   'src/**/*.{js,jsx,ts,tsx}',  // Ścieżka do plików, które mają być monitorowane
    //   '!src/**/*.d.ts',            // Wyklucz pliki TypeScript Definition
    // ],
    // coverageReporters: ['text', 'lcov', 'json'],  // Rodzaje raportów, które mają być generowane
    // coverageDirectory: 'coverage',  // Katalog, w którym będą zapisywane raporty
    testEnvironment: 'jsdom',
    setupFilesAfterEnv: ['<rootDir>/src/setupTests.js'],
    moduleNameMapper: {
      '\\.(css|scss)$': 'identity-obj-proxy', // Mockowanie stylów
    },
    collectCoverage: true,
    collectCoverageFrom: [
      'src/**/*.{js,jsx}',
      '!src/index.js',
      '!src/reportWebVitals.js',
      '!src/setupTests.js',
    ],
    coverageDirectory: 'coverage',
    coverageReporters: ['text', 'lcov', 'html'],
  };
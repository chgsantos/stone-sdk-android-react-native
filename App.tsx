import { ThemeProvider } from 'styled-components';
import Routes from '@src/routes';
import { theme } from '@src/styles/theme';
import { NavigationContainer } from '@react-navigation/native';
import React from 'react';

const App = () => {
  return (
    <ThemeProvider theme={theme}>
      <NavigationContainer>
        <Routes />
      </NavigationContainer>
    </ThemeProvider>
  );
};

export default App;

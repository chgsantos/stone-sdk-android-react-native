import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import TestPage from '@pages/test';

const Routes = () => {
  const bottomTabNavigator = createBottomTabNavigator();

  return (
    <bottomTabNavigator.Navigator>
      <bottomTabNavigator.Screen
        name="Test"
        component={TestPage}
        options={{
          title: 'Test',
          headerShown: false,
          tabBarIcon: ({ color, size }) => (
            <MaterialCommunityIcons name="cart" color={color} size={size} />
          ),
        }}
      />
    </bottomTabNavigator.Navigator>
  );
};

export default Routes;

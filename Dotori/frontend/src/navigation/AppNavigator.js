import React from "react";
import { createStackNavigator } from "@react-navigation/stack";

import LoginScreen from "../screens/Home/LoginScreen";
import SignUp1Screen from "../screens/SignUp/SignUp1Screen";
import SignUp2Screen from "../screens/SignUp/SignUp2Screen";
import SignUp3Screen from "../screens/SignUp/SignUp3Screen";
import SignUp4Screen from "../screens/SignUp/SignUp4Screen";
import SignUpCompleteScreen from "../screens/SignUp/SignUpCompleteScreen";
import MainPageScreen from "../screens/Home/MainPageScreen";
import PurposeScreen from "../screens/Purpose/PurposeScreen";
import PurposeCreate1Screen from "../screens/Purpose/PurposeCreate1Screen";
import PurposeCreate2Screen from "../screens/Purpose/PurposeCreate2Screen";
import OneCent1Screen from "../screens/OneCent/OneCent1Screen";

const Stack = createStackNavigator();

export default function AppNavigator() {
  return (
    <Stack.Navigator
      initialRouteName="LoginScreen"
      screenOptions={{ headerShown: false }}
    >
      <Stack.Screen name="LoginScreen" component={LoginScreen}></Stack.Screen>
      <Stack.Screen
        name="SignUp1Screen"
        component={SignUp1Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="SignUp2Screen"
        component={SignUp2Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="SignUp3Screen"
        component={SignUp3Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="SignUp4Screen"
        component={SignUp4Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="SignUpCompleteScreen"
        component={SignUpCompleteScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="MainPageScreen"
        component={MainPageScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PurposeScreen"
        component={PurposeScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PurposeCreate1Screen"
        component={PurposeCreate1Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PurposeCreate2Screen"
        component={PurposeCreate2Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="OneCent1Screen"
        component={OneCent1Screen}
      ></Stack.Screen>
    </Stack.Navigator>
  );
}

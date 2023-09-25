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
import PurposeCreate3Screen from "../screens/Purpose/PurposeCreate3Screen";
import PurposeCompleteScreen from "../screens/Purpose/PurposeCompleteScreen";
import PurposeDetailScreen from "../screens/Purpose/PurposeDetailScreen";
import PurposeStopScreen from "../screens/Purpose/PurposeStopScreen";
import PlanMainScreen from "../screens/Plan/PlanMainScreen";
import PlanCreate1Screen from "../screens/Plan/PlanCreate1Screen";
import PlanCreate2Screen from "../screens/Plan/PlanCreate2Screen";
import PlanCreate3Screen from "../screens/Plan/PlanCreate3Screen";
import PlanCreate4Screen from "../screens/Plan/PlanCreate4Screen";
import PlanCreate5Screen from "../screens/Plan/PlanCreate5Screen";
import PlanCategoryScreen from "../screens/Plan/PlanCategoryScreen";
import PlanNotClassifyScreen from "../screens/Plan/PlanNotClassifyScreen";
import OneCent1Screen from "../screens/OneCent/OneCent1Screen";
import OneCent2Screen from "../screens/OneCent/OneCent2Screen";
import OneCent3Screen from "../screens/OneCent/OneCent3Screen";
import OneCent4Screen from "../screens/OneCent/OneCent4Screen";
import OneCent5Screen from "../screens/OneCent/OneCent5Screen";

import MyPageScreen from "../screens/MyPage/MyPageScreen"
import PasswordChangeScreen from "../screens/MyPage/PasswordChangeScreen"
import EditPhoneNumberScreen from "../screens/MyPage/EditPhoneNumberScreen"
import EditBirthDateScreen from "../screens/MyPage/EditBirthDateScreen"
import CategoryScreen from "../screens/MyPage/CategoryScreen"
import CategorySettingScreen from "../screens/MyPage/CategorySettingScreen";
import ReceipeSelectScreen from "../screens/MyPage/ReceipeSelectScreen";
import ReceipeScreen from "../screens/MyPage/ReceipeScreen";

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
        name="PurposeCreate3Screen"
        component={PurposeCreate3Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PurposeCompleteScreen"
        component={PurposeCompleteScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PurposeDetailScreen"
        component={PurposeDetailScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PurposeStopScreen"
        component={PurposeStopScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanMainScreen"
        component={PlanMainScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanCreate1Screen"
        component={PlanCreate1Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanCreate2Screen"
        component={PlanCreate2Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanCreate3Screen"
        component={PlanCreate3Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanCreate4Screen"
        component={PlanCreate4Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanCreate5Screen"
        component={PlanCreate5Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanCategoryScreen"
        component={PlanCategoryScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="PlanNotClassifyScreen"
        component={PlanNotClassifyScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="OneCent1Screen"
        component={OneCent1Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="OneCent2Screen"
        component={OneCent2Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="OneCent3Screen"
        component={OneCent3Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="OneCent4Screen"
        component={OneCent4Screen}
      ></Stack.Screen>
      <Stack.Screen
        name="OneCent5Screen"
        component={OneCent5Screen}
      ></Stack.Screen>
      <Stack.Screen
      name="MyPageScreen"
      component={MyPageScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="PasswordChangeScreen"
      component={PasswordChangeScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="EditPhoneNumberScreen"
      component={EditPhoneNumberScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="EditBirthDateScreen"
      component={EditBirthDateScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="CategoryScreen"
      component={CategoryScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="CategorySettingScreen"
      component={CategorySettingScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="ReceipeSelectScreen"
      component={ReceipeSelectScreen}
      ></Stack.Screen>
      <Stack.Screen
      name="ReceipeScreen"
      component={ReceipeScreen}
      ></Stack.Screen>
    </Stack.Navigator>
  );
}

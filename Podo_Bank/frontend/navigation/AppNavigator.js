import React from "react";
import { createStackNavigator } from "@react-navigation/stack";
import LoginScreen from "../screens/Home/LoginScreen";
import SignupIdentityVerificationScreen from "../screens/Home/SignupIdentityVerificationScreen";
import SignupInformationScreen from "../screens/Home/SignupInformationScreen";

import SignupCompleteScreen from "../screens/Home/SignupCompleteScreen";
import HomeScreen from "../screens/Home/HomeScreen";
import AccountDetailScreen from "../screens/Account/AccountDetailScreen";
import AccountOverviewScreen from "../screens/Account/AccountOverviewScreen";
import TransferScreen from "../screens/Transfer/TransferScreen";
import TransferAmountScreen from "../screens/Transfer/TransferAmountScreen";
import TransferDetailScreen from "../screens/Transfer/TransferDetailScreen";
import TransferCompleteScreen from "../screens/Transfer/TransferCompleteScreen";
import AccountSetupScreen from "../screens/OpenAccount/AccountSetupScreen";
import AccountConfigurationScreen from "../screens/OpenAccount/AccountConfigurationScreen";
import AccountRestrictionScreen from "../screens/OpenAccount/AccountRestrictionScreen";
import OpenAccountCompleteScreen from "../screens/OpenAccount/OpenAccountCompleteScreen";
import AccountManagementScreen from "../screens/Account/AccountManagementScreen";
import TransactionDetailScreen from "../screens/Account/TransactionDetailScreen";
import MyPageScreen from "../screens/Home/MyPageScreen";

const Stack = createStackNavigator();

function AppNavigator() {
  return (
    <Stack.Navigator
      initialRouteName="LoginScreen"
      screenOptions={{ headerShown: false }}
    >
      <Stack.Screen name="LoginScreen" component={LoginScreen} />
      <Stack.Screen
        name="SignupIdentityVerificationScreen"
        component={SignupIdentityVerificationScreen}
      />
      <Stack.Screen
        name="SignupInformationScreen"
        component={SignupInformationScreen}
      />

      <Stack.Screen
        name="SignupCompleteScreen"
        component={SignupCompleteScreen}
      />
      <Stack.Screen name="HomeScreen" component={HomeScreen} />
      <Stack.Screen
        name="AccountOverviewScreen"
        component={AccountOverviewScreen}
      />
      <Stack.Screen
        name="AccountDetailScreen"
        component={AccountDetailScreen}
      />
      <Stack.Screen
        name="TransferScreen"
        component={TransferScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="TransferAmountScreen"
        component={TransferAmountScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="TransferDetailScreen"
        component={TransferDetailScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="TransferCompleteScreen"
        component={TransferCompleteScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="AccountSetupScreen"
        component={AccountSetupScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="AccountConfigurationScreen"
        component={AccountConfigurationScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="AccountRestrictionScreen"
        component={AccountRestrictionScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="OpenAccountCompleteScreen"
        component={OpenAccountCompleteScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="AccountManagementScreen"
        component={AccountManagementScreen}
      ></Stack.Screen>
      <Stack.Screen
        name="TransactionDetailScreen"
        component={TransactionDetailScreen}
      ></Stack.Screen>
      <Stack.Screen name="MyPageScreen" component={MyPageScreen}></Stack.Screen>
    </Stack.Navigator>
  );
}

export default AppNavigator;

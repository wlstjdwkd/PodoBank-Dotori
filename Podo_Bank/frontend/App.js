import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, Alert } from 'react-native';
import React, { useEffect } from 'react'
import messaging from '@react-native-firebase/messaging'
import { Provider, useDispatch, useSelector } from "react-redux"; // react-redux에서 Provider 가져오기
import store from "./src/redux/store"; // Redux 스토어 가져오기
import { NavigationContainer } from "@react-navigation/native";
import AppNavigator from "./src/navigation/AppNavigator";
import { setAccessTokenExpiration } from './src/redux/slices/auth/user';
import { setUserTokenRefreshModalVisible } from './src/redux/slices/auth/user';

function MainApp() {
  const accessTokenExpiration = useSelector(
    (state) => state.user.accessTokenExpiration
  );
  const refreshToken = useSelector((state) => state.user.refreshToken);
  const accessToken = useSelector((state) => state.user.accessToken);

  const dispatch = useDispatch();

  // accessToken 카운트다운
  useEffect(() => {
    if (accessTokenExpiration > 0) {
      const timerId = setInterval(() => {
        dispatch(setAccessTokenExpiration(accessTokenExpiration - 1));
      }, 1000);
      return () => clearInterval(timerId);
    }
    if (accessTokenExpiration === 0 && refreshToken) {
      dispatch(setUserTokenRefreshModalVisible(true));
    }
  }, [accessTokenExpiration, accessToken]);

  return (
    <>
      <NavigationContainer>
        <AppNavigator />
      </NavigationContainer>
    </>
  );
}

export default function App() {

  const requestUserPermission = async () => {
    const authStatus = await messaging().requestPermission();
    const enabled =
      authStatus === messaging.AuthorizationStatus.AUTHORIZED ||
      authStatus === messaging.AuthorizationStatus.PROVISIONAL;

    if (enabled) {
      console.log('Authorization status:', authStatus);
    }
  }

  useEffect(() => {
    if (requestUserPermission()) {
      messaging().getToken().then(token => {
        console.log(token);
      });
    } else {
      console.log("Failed token status", authStatus);
    }

    // Check whether an initial notification is available
    messaging()
      .getInitialNotification()
      .then(async (remoteMessage) => {
        if (remoteMessage) {
          console.log(
            'Notification caused app to open from quit state:',
            remoteMessage.notification,
          );
        }
      });
      
    // Assume a message-notification contains a "type" property in the data payload of the screen to open
    messaging().onNotificationOpenedApp(async (remoteMessage) => {
      console.log(
        'Notification caused app to open from background state:',
        remoteMessage.notification,
      );
    });

    // Register background handler
    messaging().setBackgroundMessageHandler(async remoteMessage => {
      console.log('Message handled in the background!', remoteMessage);
    });

    const unsubscribe = messaging().onMessage(async remoteMessage => {
      Alert.alert(remoteMessage.notification.title, 
                  remoteMessage.notification.body,
                  [
                    { text: "OK", onPress: () => console.log("OK Pressed") }
                  ]);
    });

    return unsubscribe;

  }, [])

  return (
    <Provider store={store}>
      <MainApp />
    </Provider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

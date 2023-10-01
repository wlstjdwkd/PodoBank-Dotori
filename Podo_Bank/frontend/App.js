import React, { useEffect } from "react";
import { NavigationContainer } from "@react-navigation/native";
import AppNavigator from "./src/navigation/AppNavigator";
import { Provider, useDispatch, useSelector } from "react-redux"; // react-redux에서 Provider 가져오기
import store from "./src/redux/store"; // Redux 스토어 가져오기
import AccessTokenRefreshModalScreen from "./src/screens/Modal/AccessTokenRefreshModalScreen";
// import { setAccessTokenExpiration } from '../../redux/slices/auth/user'

import { StyleSheet, Text, View, Alert } from "react-native";
import {
  setAccessTokenExpiration,
  setUserTokenRefreshModalVisible,
  setIsnotReissuanceToken,
} from "./src/redux/slices/auth/user";

// import messaging from "@react-native-firebase/messaging";

// messaging().setBackgroundMessageHandler(async (remoteMessage) => {
//   console.log("[Background Remote Message]", remoteMessage);
// });

// import { useNotifications } from "./src/hooks/useNotifications";

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
  // const getFcmToken = async () => {
  //   const fcmToken = await messaging().getToken();
  //   console.log("[FCM Token]", fcmToken);
  // };

  // useEffect(() => {
  //   getFcmToken();
  //   const unsubscribe = messaging().onMessage(async (remoteMessage) => {
  //     console.log("[Remote Message]", JSON.stringify(remoteMessage));
  //   });
  //   return unsubscribe;
  // }, []);
  // useNotifications();

  // useEffect(() => {
  // // Foreground에서 알림을 받을 때
  // const unsubscribeOnMessage = messaging().onMessage(
  //   async (remoteMessage) => {
  //     console.log("Foreground FCM message received:", remoteMessage);
  //   }
  // );

  // // 앱이 백그라운드에 있거나 종료된 상태에서 알림을 클릭하여 열었을 때
  // const unsubscribeOnNotificationOpenedApp =
  //   messaging().onNotificationOpenedApp((remoteMessage) => {
  //     console.log("Notification clicked and app opened:", remoteMessage);
  //   });

  // // 백그라운드 상태에서의 알림 처리
  // messaging().setBackgroundMessageHandler(async (remoteMessage) => {
  //   console.log("Background FCM message received:", remoteMessage);
  // });

  // // 앱이 종료된 상태에서 알림을 클릭하여 앱을 처음 열 때
  // messaging()
  //   .getInitialNotification()
  //   .then((remoteMessage) => {
  //     if (remoteMessage) {
  //       console.log(
  //         "Notification clicked and app opened from terminated state:",
  //         remoteMessage
  //       );
  //     }
  //   });

  //   return () => {
  //     // unsubscribeOnMessage();
  //     // unsubscribeOnNotificationOpenedApp();
  //   };
  // }, []);

  useEffect(() => {
    messaging()
      .getToken()
      .then((token) => {
        console.log(token);
      });

    // Check whether an initial notification is available
    messaging()
      .getInitialNotification()
      .then(async (remoteMessage) => {
        if (remoteMessage) {
          console.log(
            "Notification caused app to open from quit state:",
            remoteMessage.notification
          );
        }
      });

    //background
    // Assume a message-notification contains a "type" property in the data payload of the screen to open

    messaging().onNotificationOpenedApp(async (remoteMessage) => {
      console.log(
        "Notification caused app to open from background state:",
        remoteMessage.notification
      );
      // navigation.navigate(remoteMessage.data.type);
    });

    // 백그라운드 상태에서의 알림 처리
    messaging().setBackgroundMessageHandler(async (remoteMessage) => {
      console.log("Background FCM message received:", remoteMessage);
    });

    const unsubscribe = messaging().onMessage(async (remoteMessage) => {
      Alert.alert("A new FCM message arrived!", JSON.stringify(remoteMessage));
    });

    return unsubscribe;
  }, []);

  return (
    <Provider store={store}>
      <MainApp />
    </Provider>
  );
}

// import React from "react"
// import { NavigationContainer } from "@react-navigation/native"
// import AppNavigator from "./src/navigation/AppNavigator"
// import { Provider, useSelector } from "react-redux" // react-redux에서 Provider 가져오기
// import store from "./src/redux/store" // Redux 스토어 가져오기
// import AccessTokenRefreshModalScreen from "./src/screens/Modal/AccessTokenRefreshModalScreen"

// export default function App() {
//   const accessTokenExpiration = useSelector((state) => state.user.accessTokenExpiration)

//   return (
//     <Provider store={store}>
//       <NavigationContainer>
//         <AppNavigator />
//         {accessTokenExpiration && <AccessTokenRefreshModalScreen />}
//       </NavigationContainer>
//     </Provider>
//   );
// }

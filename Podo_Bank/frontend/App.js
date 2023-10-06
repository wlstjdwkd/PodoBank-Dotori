import React, { useEffect } from "react";
import { NavigationContainer } from "@react-navigation/native";
import AppNavigator from "./src/navigation/AppNavigator";
import { Provider, useDispatch, useSelector } from "react-redux"; // react-redux에서 Provider 가져오기
import store from "./src/redux/store"; // Redux 스토어 가져오기
import AccessTokenRefreshModalScreen from "./src/screens/Modal/AccessTokenRefreshModalScreen";
// import { setAccessTokenExpiration } from '../../redux/slices/auth/user'
import {
  setAccessTokenExpiration,
  setUserTokenRefreshModalVisible,
  setIsnotReissuanceToken,
} from "./src/redux/slices/auth/user";

// import * as Notifications from "expo-notifications";

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
  // useEffect(() => {
  //   const subscription = Notifications.addNotificationReceivedListener(
  //     (notification) => {
  //       console.log("Notification received: ", notification);
  //     }
  //   );

  //   return () => subscription.remove();
  // }, []);

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

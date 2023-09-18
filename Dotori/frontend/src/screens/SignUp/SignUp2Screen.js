import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";

export default function SignUp2Screen({ navigation, route }) {
  const [userInfo, setUserInfo] = useState(route.params.userInfo);
  const [isValidEmail, setIsValidEmail] = useState(false);

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="회원가입(2/4)"
        cancelNavi="LoginScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.header}>
        <Text style={styles.title}>인증 코드 입력하기</Text>
        <View style={styles.subtitle}>
          <Text style={styles.grayText}>이메일로 인증된 코드를</Text>
          <Text style={styles.grayText}>하단에 입력해주세요</Text>
        </View>
        <View style={styles.inputContainer}>
          <TextInput style={styles.input} />
          <TouchableOpacity style={styles.verifyButton}>
            <Text style={styles.verifyButtonText}>인증하기</Text>
          </TouchableOpacity>
        </View>
        <TouchableOpacity style={styles.resendButton}>
          <Text style={styles.resendButtonText}>인증 코드 재전송</Text>
        </TouchableOpacity>
      </View>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          navigation.navigate("SignUp3Screen", { userInfo: userInfo });
        }}
        //TODO: 풀기
        // disabled={!isValidEmail}
      >
        <Text style={styles.buttonText}>다음</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    marginTop: 90,
  },
  grayText: {
    color: "#7B7B7B",
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  subtitle: {
    marginBottom: 30,
  },
  input: {
    flex: 1,
    height: 50,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    // textAlign: "center",
  },
  button: {
    height: 50,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    width: "100%",
  },
  verifyButton: {
    backgroundColor: "#FF965C",
    borderRadius: 10,
    marginLeft: 10,
    paddingHorizontal: 10,
    paddingVertical: 5,
    justifyContent: "center",
    alignItems: "center",
    height: 50,
  },
  verifyButtonText: {
    color: "white",
    fontSize: 16,
  },
  resendButton: {
    alignItems: "flex-end",
    marginTop: 10,
  },
  resendButtonText: {
    color: "#FF965C",
  },
});

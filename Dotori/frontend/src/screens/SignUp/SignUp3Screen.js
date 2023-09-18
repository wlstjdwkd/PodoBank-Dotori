import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";

export default function SignUp3Screen({ navigation, route }) {
  const [userInfo, setUserInfo] = useState(route.params.userInfo);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");
  const [confirmPasswordMessage, setConfirmPasswordMessage] = useState("");
  const [isPasswordValid, setIsPasswordValid] = useState(false);
  const [isConfirmPasswordValid, setIsConfirmPasswordValid] = useState(false);

  const validatePassword = (password) => {
    const regex =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
    return regex.test(password);
  };
  const handlePasswordChange = (text) => {
    setPassword(text);
    if (validatePassword(text)) {
      setPasswordMessage("완벽합니당");
      setIsPasswordValid(true);
    } else {
      setPasswordMessage("양식을 맞춰주세요!");
      setIsPasswordValid(false);
    }
  };

  const handleConfirmPasswordChange = (text) => {
    setConfirmPassword(text);
    if (text === password && isPasswordValid) {
      setConfirmPasswordMessage("완벽합니당");
      setIsConfirmPasswordValid(true);
      setUserInfo((prev) => ({ ...prev, password: text }));
    } else {
      setConfirmPasswordMessage("비밀번호가 일치하지 않습니다");
      setIsConfirmPasswordValid(false);
    }
  };

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="회원가입(3/4)"
        cancelNavi="LoginScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.header}>
        <Text style={styles.title}>비밀번호 설정하기</Text>
        <Text style={styles.subtitle}>비밀번호</Text>
        <TextInput
          style={styles.input}
          onChangeText={handlePasswordChange}
          multiline={true}
          secureTextEntry={true}
        />
        <Text style={styles.inputBehindText}>
          영문, 숫자, 특수문자 포함 8자 이상
        </Text>
        <Text
          style={{
            color: isPasswordValid ? "blue" : "red",
            marginLeft: 30,
            marginTop: -30,
          }}
        >
          {passwordMessage}
        </Text>

        <Text style={styles.subtitle}>비밀번호 확인</Text>
        <TextInput
          style={styles.input}
          onChangeText={handleConfirmPasswordChange}
          multiline={true}
          secureTextEntry={true}
        />
        <Text style={styles.inputBehindText}>
          영문, 숫자, 특수문자 포함 8자 이상
        </Text>
        <Text
          style={{
            color: isConfirmPasswordValid ? "blue" : "red",
            marginLeft: 30,
            marginTop: -30,
          }}
        >
          {confirmPasswordMessage}
        </Text>
      </View>

      <TouchableOpacity
        style={styles.button}
        onPress={() =>
          navigation.navigate("SignUp2Screen", { userInfo: userInfo })
        }
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
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 40,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 10,
  },
  input: {
    width: "100%",
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
  inputBehindText: {
    color: "#7B7B7B",
    marginBottom: 40,
    fontSize: 12,
    marginLeft: 10,
  },
});

import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";

export default function SignUp1Screen({ navigation }) {
  const [userInfo, setUserInfo] = useState({
    email: "",
  });

  const [email, setEmail] = useState("");
  const [isValidEmail, setIsValidEmail] = useState(""); // 양식에맞게 이메일 작성
  const [isCorrectEmail, setIsCorrectEmail] = useState(""); // 사용가능한 이메일여부 확인
  const [emailMessage, setEmailMessage] = useState(""); // E-mail 사용가능 여부를 나타내는 메시지
  function validateEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
  }

  const handleEmailChange = (text) => {
    setEmail(text);
    if (validateEmail(text)) {
      setEmailMessage("사용 가능한 이메일입니다.");
      setIsValidEmail(true);
      // setEmailDuplicatedCheck(null); // 초기화: 이메일 양식이 올바르므로 중복 확인을 다시 해야 함
      // setIsCorrectEmail(null);
    } else {
      setEmailMessage("이메일 양식을 맞춰주세요!");
      setIsValidEmail(false);
      // setEmailDuplicatedCheck(null); // 초기화: 이메일 양식이 올바르지 않으므로 중복 확인을 다시 해야 함
      // setIsCorrectEmail(null);
    }
  };
  // const hanldeUserEmailDuplicationCheck = async (text) => {
  //   if (validateEmail(text)) {
  //     const response = await userEmailDuplicationCheck(text);
  //     if (response.status === 200) {
  //       setEmailMessage("사용 가능한 이메일입니다.");
  //       // setEmailDuplicatedCheck(true);
  //       setIsCorrectEmail(true);
  //     } else if (response.status === 400) {
  //       setEmailMessage("이미 사용 중인 이메일입니다.");
  //       // setEmailDuplicatedCheck(false);
  //       setIsCorrectEmail(false);
  //     } else {
  //       setEmailMessage("서버 오류로 중복 확인에 실패했습니다.");
  //       // setEmailDuplicatedCheck(null);
  //       setIsCorrectEmail(null);
  //     }
  //   } else {
  //     setEmailMessage("이메일 양식을 맞춰주세요!");
  //     // setEmailDuplicatedCheck(null);
  //     setIsCorrectEmail(null);
  //   }
  // };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="회원가입(1/4)"
        cancelNavi="LoginScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <View style={styles.header}>
          <Text style={styles.title}>이메일 인증하기</Text>
          <Text style={styles.subtitle}>사용 가능한 이메일을 입력해주세요</Text>
          <TextInput
            style={styles.input}
            placeholder="이메일 예) abc123@naver.com"
            onChangeText={(text) => {
              handleEmailChange(text);
              // {hanldeUserEmailDuplicationCheck(text) && isValidEmail}
              setUserInfo((prev) => ({ ...prev, email: text }));
            }}
            multiline={true}
            keyboardType="email-address"
          />
          <Text
            style={{
              color: isCorrectEmail && isValidEmail ? "blue" : "red",
              marginLeft: 30,
              marginTop: 0,
            }}
          >
            {emailMessage}
          </Text>
        </View>

        <TouchableOpacity
          style={styles.button}
          onPress={() =>
            navigation.navigate("SignUp2Screen", { userInfo: userInfo })
          }
          //TODO: 풀기
          // disabled={!isValidEmail}
        >
          <Text style={styles.buttonText}>메일 보내기</Text>
        </TouchableOpacity>
      </View>
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
  innerContainer: {
    flex: 1,
    paddingHorizontal: 20,
    marginTop: 20,
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
    marginBottom: 10,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 30,
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
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 10,
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
});

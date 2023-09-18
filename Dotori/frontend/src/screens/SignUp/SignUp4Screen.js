import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";

export default function SignUp4Screen({ navigation, route }) {
  const [userInfo, setUserInfo] = useState(route.params.userInfo);
  const [nameMessage, setNameMessage] = useState("");
  const [birthDateMessage, setBirthDateMessage] = useState("");
  const [phoneNumberMessage, setPhoneNumberMessage] = useState("");

  const [isValid, setIsValid] = useState({
    isNameValid: false,
    isBirthdateValid: false,
    isPhoneNumberValid: false,
  });

  const validateName = (text) => {
    const regex = /^[A-Za-z가-힣]{2,}$/;
    return regex.test(text);
  };
  const validateBirthDate = (text) => {
    const regex = /^\d{8}$/;
    return regex.test(text);
  };
  const validatePhoneNumber = (text) => {
    const regex = /^01\d{9}$/;
    return regex.test(text);
  };
  const handleNameChange = (text) => {
    if (validateName(text)) {
      setNameMessage("이름 작성 완료");
      setIsValid((prev) => ({ ...prev, isNameValid: true }));
      setUserInfo({ ...userInfo, name: text });
    } else {
      setNameMessage("이름은 2글자 이상이어야합니다.");
      setIsValid((prev) => ({ ...prev, isNameValid: false }));
    }
  };
  const handleBirthDateChange = (text) => {
    if (validateBirthDate(text)) {
      setBirthDateMessage("생년월일 작성 완료");
      setIsValid((prev) => ({ ...prev, isBirthdateValid: true }));
      setUserInfo({ ...userInfo, birthDate: text });
    } else {
      setBirthDateMessage("8자리 숫자로 작성해야 합니다.");
      setIsValid((prev) => ({ ...prev, isBirthdateValid: false }));
    }
  };
  const handlePhoneNumberChange = (text) => {
    if (validatePhoneNumber(text)) {
      setPhoneNumberMessage("휴대폰번호 작성 완료");
      setIsValid((prev) => ({ ...prev, isPhoneNumberValid: true }));
      setUserInfo({ ...userInfo, phoneNumber: text });
    } else {
      setPhoneNumberMessage("01X로 시작하는 11자리 숫자로 작성해야 합니다.");
      setIsValid((prev) => ({ ...prev, isPhoneNumberValid: false }));
    }
  };

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="회원가입(4/4)"
        cancelNavi="LoginScreen"
        navigation={navigation}
      ></HeaderComponent>
      <ScrollView style={styles.header}>
        <Text style={styles.title}>가입정보 입력하기</Text>
        <View style={styles.rowContainer}>
          <Text style={styles.subtitle}>이름</Text>
          <Text
            style={{
              color: isValid.isNameValid ? "blue" : "red",
              //   marginLeft: 150,
            }}
          >
            {nameMessage}
          </Text>
        </View>

        <TextInput
          style={styles.input}
          onChangeText={handleNameChange}
          multiline={true}
        />

        <View style={styles.rowContainer}>
          <Text style={styles.subtitle}>생년월일</Text>
          <Text
            style={{
              color: isValid.isBirthdateValid ? "blue" : "red",
              //   marginLeft: 150,
            }}
          >
            {birthDateMessage}
          </Text>
        </View>

        <TextInput
          style={styles.input}
          onChangeText={handleBirthDateChange}
          multiline={true}
        />

        <Text style={styles.inputBehindText}>생년월일 8자리 예) 19991212</Text>

        <View style={styles.rowContainer}>
          <Text style={styles.subtitle}>휴대폰번호</Text>
          <Text
            style={{
              color: isValid.isPhoneNumberValid ? "blue" : "red",
              //   marginLeft: 0,
            }}
          >
            {phoneNumberMessage}
          </Text>
        </View>

        <TextInput
          style={styles.input}
          onChangeText={handlePhoneNumberChange}
          multiline={true}
        />
      </ScrollView>

      <TouchableOpacity
        style={styles.button}
        onPress={() =>
          navigation.navigate("SignUpCompleteScreen", { name: userInfo.name })
        }

        //TODO: DISABLED 풀기

        // disabled={
        //   !isValid.isNameValid ||
        //   !isValid.isBirthdateValid ||
        //   !isValid.isPhoneNumberValid
        // }
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
    marginBottom: 20,
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
    marginTop: -20,
  },
  rowContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
});

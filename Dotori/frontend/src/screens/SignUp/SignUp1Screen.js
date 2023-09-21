import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";
import {userIdDuplicatedCheck} from "../../apis/userapi"

export default function SignUp1Screen({ navigation }) {
  const [userInfo, setUserInfo] = useState({
    email: "",
  });

  const [email, setEmail] = useState("");
  const [isValidEmail, setIsValidEmail] = useState(false); // 양식에맞게 이메일 작성
  const [isCorrectEmail, setIsCorrectEmail] = useState(false); // 사용가능한 이메일여부 확인
  const [emailMessage, setEmailMessage] = useState(""); // E-mail 사용가능 여부를 나타내는 메시지
  const [emailDuplicatedCheck, setEmailDuplicatedCheck] = useState(false)
  const [isemailSend, setIsemailSend] = useState(false)


  function validateEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
  }

  const handleEmailChange = (text) => {
    setEmail(text);
    setUserInfo((prev) => ({ ...prev, email: text })) // 중복 체크 연동 성공하면 이건 삭제
    if (validateEmail(text)) {
      setEmailMessage("사용 가능한 이메일입니다.")
      setIsValidEmail(true)
    } else {
      setEmailMessage("이메일 양식을 맞춰주세요!")
      setIsValidEmail(false)
      setEmailDuplicatedCheck(false)
      setIsCorrectEmail(false)
    }
  };
  const hanldeuserIdDuplicatedCheck = async (text) => {
    if (validateEmail(text)) {
      const response = await userIdDuplicatedCheck(text);
      if (response.status === 200) {
        setEmailMessage("사용 가능한 이메일입니다.")
        setEmailDuplicatedCheck(true)
        setIsCorrectEmail(true)
        setUserInfo((prev) => ({ ...prev, email: text }));
      } else if (response.status === 400) {
        setEmailMessage("이미 사용 중인 이메일입니다.")
        setEmailDuplicatedCheck(false);
        setIsCorrectEmail(false);
      } else {
        setEmailMessage("서버 오류로 중복 확인에 실패했습니다.")
        setEmailDuplicatedCheck(null)
        setIsCorrectEmail(null)
      }
    } else {
      setEmailMessage("이메일 양식을 맞춰주세요!")
      setEmailDuplicatedCheck(null)
      setIsCorrectEmail(null)
    }
  };

  const handleSendEmail = () => {
    Alert.alert("","ㅇㅋ 이메일 보냈다 침")
    console.log(userInfo)
    setIsemailSend(true)
    navigation.navigate("SignUp2Screen", { userInfo: userInfo })
    //  if(isCorrectEmail){
    //    const response = await 어쩌고저쩌고()
    //    if(response.status === 200){
    //      navigation.navigate("SignUp2Screen", { userInfo: userInfo })
    //    }else if(response.status === 400){
    //      console.log("이메일 전송 실패")
    //    }
    //  }else{
    //    console.log('이메일 못보내!')
    //    Alert.alert('이메일 못보내!')
    //  }
  }

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
              hanldeuserIdDuplicatedCheck(text)
              // setUserInfo((prev) => ({ ...prev, email: text }));
            }}
            // multiline={true}
            keyboardType="email-address"
            onSubmitEditing={()=>{
              // if(isConfirmPasswordValid){
              //   gotoSignUp4Screen()
              //   // navigation.navigate("SignUp4Screen", { userInfo: userInfo })
              // }
              handleSendEmail()
            }}
          />
          <Text
            style={{
              color: isCorrectEmail && isValidEmail ? "blue" : "red",
              // marginLeft: 30,
              marginTop: 0,
            }}
          >
            {emailMessage}
          </Text>
        </View>

        <TouchableOpacity
          style={[styles.button,
            // !(isValidEmail) && {backgroundColor: "grey",},
            !(isCorrectEmail) && {backgroundColor: "grey",},
          ]}
          onPress={() => {
              handleSendEmail()
          }}
          // disabled={!isValidEmail}
          // disabled={!isCorrectEmail} // 이것을 쓰는게 맞을걸?
        >
          {/* <Text style={styles.buttonText}>{isemailSend?"인증하러 가기":"메일 보내기"}</Text> */}
          <Text style={styles.buttonText}>인증메일 보내기</Text>
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

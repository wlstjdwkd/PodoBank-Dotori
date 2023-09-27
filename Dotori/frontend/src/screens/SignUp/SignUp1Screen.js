import React, { useEffect, useRef, useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";
import {userIdDuplicatedCheck, userSendEmail} from "../../apis/userapi"

export default function SignUp1Screen({ navigation }) {
  const emailRef = useRef(null)
  const [userInfo, setUserInfo] = useState({
    id: "",
  });

  const [email, setEmail] = useState("");
  const [isValidEmail, setIsValidEmail] = useState(false); // 사용가능한 이메일인지 확인
  const [emailMessage, setEmailMessage] = useState(""); // E-mail 사용가능 여부를 나타내는 메시지
  // const [cannotUseEmail, setCannotUseEmail] = useState([])  // 중복 검사 이후, 중복된 녀석은 여기에 넣어두기.
  // isCanEmail을 이용해서 네트워크 등 실패시 emailMessage 파란색으로 바꿔줘야함.
  const [isCanEmail, setisCanEmail] = useState(true)

  function validateEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
  }

  const handleEmailChange = (text) => {
    setEmail(text);
    setUserInfo((prev) => ({ ...prev, id: text })) // 중복 체크 연동 성공하면 이건 삭제
    if (validateEmail(text)) {
      setEmailMessage("사용 가능한 이메일입니다.")
      setIsValidEmail(true)
      setisCanEmail(true)
    } else {
      setEmailMessage("이메일 양식을 맞춰주세요!")
      setIsValidEmail(false)
    }
  };
  // const handleEmailChange = (text) => {
  //   setEmail(text);
  //   setUserInfo((prev) => ({ ...prev, id: text })) // 중복 체크 연동 성공하면 이건 삭제
  //   const updatedCannotUseEmail = [...cannotUseEmail, text]
  //   console.log(updatedCannotUseEmail)
  //   if (validateEmail(text)) {
  //     if(updatedCannotUseEmail.includes(text)){
  //       setEmailMessage("사용할 수 없는 이메일입니다.")
  //       setIsValidEmail(false)
  //     }else{
  //       setEmailMessage("사용 가능한 이메일입니다.")
  //       setIsValidEmail(true)
  //     }
  //   } else {
  //     setEmailMessage("이메일 양식을 맞춰주세요!")
  //     setIsValidEmail(false)
  //   }
  // };
  const handleSendEmail = () =>{
    if(isValidEmail){
      // setEmailMessage("코드 전송 중...")
      doSendEmail()
    }else{
      setEmailMessage("이메일 주소를 다시 확인해주세요")
      setIsValidEmail(false)
    }
  }

  // const doSendEmail = async () => {
  //   Alert.alert('','되었다 생각하고 넘기기')
  //   navigation.navigate("SignUp2Screen", { userInfo: userInfo })
  // }
  const doSendEmail = async () => {
    setEmailMessage("코드 전송 중.")
    const intervalId = setInterval(() => {
      setEmailMessage(prevMessage => {
        // 메시지에 점을 추가하면서 이메일 전송 또 하지 않게 눈으로 보여주기.
        if (prevMessage.length >= 10) {
          return "코드 전송 중."
        } else {
          return prevMessage + "."
        }
      })
    }, 500)
    try{
      const response = await userSendEmail(email)
      if(response.status === 200){
        setEmailMessage("사용 가능한 이메일입니다.")
        setisCanEmail(true)
        navigation.navigate("SignUp2Screen", { userInfo: userInfo })
      }else if(response.status === 409){
        setEmailMessage('이미 사용중인 이메일입니다.')
        setisCanEmail(false)
        // setCannotUseEmail((prev) => {[...prev, email]}) // 이메일이 중복되었으니 이것은 사용하지 못하도록 우선 넣어둠.
        // console.log(cannotUseEmail)
        console.log("사용중인 이메일입니다.")
      }else{
        console.log("오류 발생 : 이메일 전송 실패")
        setEmailMessage("오류 발생 : 이메일 전송 실패")
        setisCanEmail(false)
      }
    }catch(error){
      setisCanEmail(false)
      console.log("에러..",error)
      setEmailMessage("오류 발생: 이메일 전송 실패")
    }
    clearInterval(intervalId)
  }

  useEffect(() =>{
    emailRef.current.focus()
  })

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
            }}
            // multiline={true}
            ref={emailRef}
            returnKeyType="send"
            keyboardType="email-address"
            onSubmitEditing={()=>{
              handleSendEmail()
            }}
          />
          <Text
            style={{
              color: (isValidEmail&&isCanEmail) ? "blue" : "red",
              marginTop: 0,
            }}
          >
            {emailMessage}
          </Text>
        </View>

        <TouchableOpacity
          style={[styles.button,
            !(isValidEmail) && {backgroundColor: "grey",},
          ]}
          onPress={() => {
              handleSendEmail()
          }}
          disabled={!isValidEmail}
        >
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

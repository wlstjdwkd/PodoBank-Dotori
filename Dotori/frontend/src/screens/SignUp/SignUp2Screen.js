import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";
// userInfoInquiry 이거 아님 바꿔야함.
import {userInfoInquiry} from "../../apis/userapi"

export default function SignUp2Screen({ navigation, route }) {
  const [userInfo, setUserInfo] = useState(route.params.userInfo);
  const [emailVerificationCode, setEmailVerificationCode] = useState("")
  const [isValidEmailCode, setIsValidEmailCode] = useState(false);
  // 이메일 인증 카운트 다운
  const [countdown, setCountdown] = useState(5); // 5분 = 300초\
  const [startCountdown, setStartCountdown] = useState(true);
  const [codeMessage, setCodeMessage] = useState("작동확인중")


  // 이메일 인증 코드 확인
  const handleEmailCodeVerificate = () =>{
    setCodeMessage("인증 완료")
    setIsValidEmailCode(true)
    
    // const response = userInfoInquiry()  // 지금 여기로 보내는거 아님.
    // if(response.status === 200){
    //   console.log("이메일 인증 코드 확인 성공")
    //   setCodeMessage("인증 완료")
    //   setIsValidEmailCode(true)
    // }else if(response.status === 400){
    //   console.log("이메일 인증 코드 확인 실패")
    //   setCodeMessage("인증에 실패하셨습니다.")
    // }
  }

  const handleSendEmail = async () => {
    setStartCountdown(false)
    //인증되면 풀어두기
    setCountdown(5)
    setStartCountdown(true)
    // const response = await 어쩌고()
    // if(response.status===200){
    //   console.log("이메일 전송 성공")
    //   setCountdown(300)
    //   setStartCountdown(true)
    // }else if(response.status===400){
    //   console.log("이메일 전송 실패")
    // }
  }

  useEffect(() => {
    let intervalId;
    
    if (startCountdown && countdown > 0) {
        intervalId = setInterval(() => {
            setCountdown(countdown => countdown - 1)
        }, 1000)
    } else if (startCountdown===true && countdown === 0) {
        clearInterval(intervalId)
        setCodeMessage('이메일 인증시간 만료')
    }
    return () => clearInterval(intervalId); // 컴포넌트 unmount 시 타이머 해제
  }, [startCountdown, countdown])

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="회원가입(2/4)"
        cancelNavi="LoginScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <View style={styles.header}>
          <Text style={styles.title}>인증 코드 입력하기</Text>
          <View style={styles.subtitle}>
            <Text style={styles.grayText}>이메일로 인증된 코드를</Text>
            <Text style={styles.grayText}>하단에 입력해주세요</Text>
          </View>
          <View style={[styles.inputContainer]}>
            <View style={[styles.input, {flexDirection:'row'}]}>
              <TextInput
                style={{flex:1}}
                onChangeText={(text)=>{
                  setEmailVerificationCode(text)
                }}
                value={emailVerificationCode}
                // maxLength={16}
                keyboardType="number-pad"
                returnKeyType ="next"
                // ref={passwordInputRef}
                // onSubmitEditing={()=>{
                  // confirmPasswordInputRef.current.focus()
                // }}
                maxLength={8}
              />
              <View style={{alignItems:'flex-end'}}><Text>{`${Math.floor(countdown / 60)}:${countdown % 60 < 10 ? '0' : ''}${countdown % 60}`}</Text></View>
            </View>
            <TouchableOpacity style={styles.verifyButton}
              onPress={()=>{
                handleEmailCodeVerificate()
              }}
            >
              <Text style={styles.verifyButtonText}>인증하기</Text>
            </TouchableOpacity>
          </View>
          <View style={styles.referenceMessage}>
            <Text
              style={{color:isValidEmailCode?"blue":"red",}}
            >
              {codeMessage}
            </Text>
            <TouchableOpacity style={styles.resendButton}
              onPress={()=>{
                handleSendEmail()
              }}
            >
              <Text style={styles.resendButtonText}>인증 코드 재전송</Text>
            </TouchableOpacity>
          </View>
        </View>
        <TouchableOpacity
          style={[styles.button,
            !(isValidEmailCode) && {backgroundColor: "grey",},
          ]}
          onPress={() => {
            navigation.navigate("SignUp3Screen", { userInfo: userInfo });
          }}
          // disabled={!isValidEmail}
        >
          <Text style={styles.buttonText}>다음</Text>
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
  grayText: {
    color: "#7B7B7B",
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
    marginTop: 20,
  },
  subtitle: {
    marginBottom: 30,
  },
  input: {
    flex: 1,
    height: 40,
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
    width: "30%",
    justifyContent: "center",
    alignItems: "center",
    height: 40,
  },
  verifyButtonText: {
    color: "white",
    fontSize: 16,
  },
  resendButton: {
    alignItems: "flex-end",
    // marginTop: 20,
    // borderBottomWidth: 1,
  },
  resendButtonText: {
    color: "#5A5A5A",
    borderBottomWidth: 1,
  },
  referenceMessage:{
    flexDirection:"row", 
    justifyContent:'space-between', 
    marginTop: 20,
  }
});

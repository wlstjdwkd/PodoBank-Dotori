import React, { useEffect, useRef, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  TextInput,
  Button,
  Alert,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import {userPasswordChange} from "../../apis/userapi"

export default function PasswordChangeScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const currentPasswordRef = useRef(null)
  const changePasswordRef = useRef(null)
  const changeConfirmPasswordRef = useRef(null)
  const [currentPassword, setCurrentPassword] = useState("")
  const [changePassword, setChangePassword] = useState("")
  const [changeConfirmPassword, setChangeConfirmPassword] = useState("")
  const [pwMessage, setPwMessage] = useState({
    currentPwMessage : "",
    changePwMessage : "",
    confirmChangePwMessage : "",
    responseMessage : "비밀번호 변경 중",
  })
  const [isValidPw, setIsValidPw] = useState({
    isCurrentPwValid: false,
    isChangePwValid: false,
    isConfirmPwValid: false,
    isCanChange: false,
    responseResult: true,
  })



  const validatePassword = (password) => {
    const regex =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
    return regex.test(password);
  };
  const handlePasswordCurrentCheck = (text) => {
    setCurrentPassword(text);
    if (validatePassword(text)) {
      setPwMessage((prev)=>({...prev, currentPwMessage:''}));
      setIsValidPw((prev)=>({...prev, isCurrentPwValid:true}));
    } else {
      setPwMessage((prev)=>({...prev, currentPwMessage:'비밀번호를 다시 확인해주세요!'}));
      setIsValidPw((prev)=>({...prev, isCurrentPwValid:false}));
    }
  };

  const handlePasswordChangeCheck = (text) => {
    setChangePassword(text);
    if (validatePassword(text) && text != currentPassword) {
      setPwMessage((prev)=>({...prev, changePwMessage:'완벽합니다.'}));
      setIsValidPw((prev)=>({...prev, isChangePwValid:true}));
    } else if(text === currentPassword){
      setPwMessage((prev)=>({...prev, changePwMessage:'이전과 동일한 비밀번호는 사용할 수 없습니다.'}));
      setIsValidPw((prev)=>({...prev, isChangePwValid:false}));
    }else {
      setPwMessage((prev)=>({...prev, changePwMessage:'양식을 맞춰주세요!'}));
      setIsValidPw((prev)=>({...prev, isChangePwValid:false}));
    }
  };

  const handleConfirmPasswordChangeCheck = (text) => {
    setChangeConfirmPassword(text);
    if ((text === changePassword) && validatePassword(text) && text != currentPassword) {
      setPwMessage((prev)=>({...prev, confirmChangePwMessage:'비밀번호가 일치합니다.'}));
      setIsValidPw((prev)=>({...prev, isConfirmPwValid:true}));
      // setUserInfo((prev) => ({ ...prev, password: text }));
    } else if(text === currentPassword){
      setPwMessage((prev)=>({...prev, confirmChangePwMessage:'이전과 동일한 비밀번호는 사용할 수 없습니다.'}));
      setIsValidPw((prev)=>({...prev, isConfirmPwValid:false}));
    }else {
      setPwMessage((prev)=>({...prev, confirmChangePwMessage:'비밀번호가 일치하지 않습니다.'}));
      setIsValidPw((prev)=>({...prev, isConfirmPwValid:false}));
    }
  };



  // 비밀번호 변경 함수
  const handleUserPasswordChange = () => {
    if(isValidPw.isCanChange){
      doUserPasswordChange()
    }else{
      switch (!isValidPw.isConfirmPwValid) {
        case !isValidPw.isCurrentPwValid:
          setPwMessage((prev)=>({...prev, responseMessage:"현재 비밀번호를 확인해주세요."}))
          currentPasswordRef.current.focus()
          break;
        case !isValidPw.isChangePwValid:
          setPwMessage((prev)=>({...prev, responseMessage:"변경할 비밀번호를 확인해주세요."}))
          changePasswordRef.current.focus()
          break;
        // case !isValidPw.isConfirmPwValid:
        //   setPwMessage((prev)=>({...prev, responseMessage:"비밀번호 확인 부분을 확인해주세요."}))
        //   changeConfirmPasswordRef.current.focus()
        //   break;
          
        default:
          setPwMessage((prev)=>({...prev, responseMessage:"비밀번호 확인 부분을 확인해주세요."}))
          changeConfirmPasswordRef.current.focus()
          break;
      }
    }
    
  }


  const doUserPasswordChange = async () =>{
    const data =   {
      "beforePassword" : currentPassword,
      "afterPassword" : changeConfirmPassword,
    }
    try{
      const response = await userPasswordChange(data, accessToken, grantType)
      if(response.status===200){
        console.log('비밀번호 변경 성공')
        setIsValidPw((prev)=>({...prev, responseResult:true}));
        setPwMessage((prev)=>({...prev, responseMessage:"비밀번호가 변경되었습니다."}))
        Alert.alert('','비밀번호가 변경되었습니다.')
        navigation.navigate("MyPageScreen");
      }else if(response.status===403){
        setIsValidPw((prev)=>({...prev, responseResult:false}));
        console.log('비밀번호 변경 실패',response.status)
        console.log(response.data)
        setIsValidPw((prev)=>({...prev, responseResult:false}));
        setPwMessage((prev)=>({...prev, responseMessage:response.data}))
      }else if(response.status===404){
        console.log('비밀번호 변경 실패',response.status)
        console.log(response.data)
        setIsValidPw((prev)=>({...prev, responseResult:false}));
        setPwMessage((prev)=>({...prev, responseMessage:response.data}))
      }else{
        setIsValidPw((prev)=>({...prev, responseResult:false}));
        console.log('오류 발생 : 비밀번호 변경 실패',response.status)
      }
    }catch(error){
      setIsValidPw((prev)=>({...prev, responseResult:false}));
      console.log("오류 발생 : 비밀번호 변경 실패", error)
    }
  }
  useEffect(()=>{
    currentPasswordRef.current.focus()
  },[])

  useEffect(()=>{
    if(isValidPw.isCurrentPwValid && isValidPw.isChangePwValid && isValidPw.isConfirmPwValid){
      setIsValidPw((prev) => ({...prev, isCanChange:true}))
      setPwMessage((prev)=>({...prev, responseMessage:"비밀번호 변경 가능"}))
    }else{
      setPwMessage((prev)=>({...prev, responseMessage:"비밀번호 변경 중"}))
      setIsValidPw((prev) => ({...prev, isCanChange:false}))
    }
    console.log(isValidPw)
  },[isValidPw.isCurrentPwValid, isValidPw.isChangePwValid, isValidPw.isConfirmPwValid])
  return (
    <View style={styles.container}>
      <HeaderComponent title="비밀번호 변경" navigation={navigation}/>

      <View style={styles.iconContainer}>
        <Image
          style={styles.lockIcon}
          source={require("../../assets/icon/lock.png")} // 프로필 이미지 경로
        />
      </View>

      <View style={styles.passwordInput}>
        <Text style={styles.passwordPlaceholder}>*</Text>
        <Text style={styles.passwordPlaceholder}>*</Text>
        <Text style={styles.passwordPlaceholder}>*</Text>
        <Text style={styles.passwordPlaceholder}>*</Text>
      </View>

      {/* 비밀번호 안내 텍스트 */}
      <View style={styles.passwordInfoContainer}>
        <Text style={styles.passwordChangeInfoText}>
          비밀번호를 변경해 주세요!
        </Text>
        <Text style={styles.passwordInfoText}>
          기존의 비밀번호와 새 비밀번호를 입력해주세요.
        </Text>
      </View>

      {/* 텍스트 입력란 */}
      <TextInput
        style={styles.inputBox}
        placeholder="현재 비밀번호"
        secureTextEntry={true} // 비밀번호 숨김 처리
        returnKeyType="next"
        ref={currentPasswordRef}
        value={currentPassword}
        onChangeText={(text)=>{
          handlePasswordCurrentCheck(text)
        }}
        maxLength={16}
        onSubmitEditing={()=>{
          changePasswordRef.current.focus()
        }}
      />
      <Text
        style={[styles.pwMessage, {
          color: (isValidPw.isCurrentPwValid) ? "blue" : "red",
        }]}
      >
        {pwMessage.currentPwMessage}
      </Text>

      <TextInput
        style={styles.inputBox}
        placeholder="새 비밀번호"
        secureTextEntry={true} // 비밀번호 숨김 처리
        returnKeyType="next"
        ref={changePasswordRef}
        value={changePassword}
        onChangeText={(text)=>{
          handlePasswordChangeCheck(text)
        }}
        maxLength={16}
        onSubmitEditing={()=>{
          changeConfirmPasswordRef.current.focus()
        }}
      />
      <Text
        style={[styles.pwMessage, {
          color: (isValidPw.isChangePwValid) ? "blue" : "red",
        }]}
      >
        {pwMessage.changePwMessage}
      </Text>

      <TextInput
        style={styles.inputBox}
        placeholder="비밀번호 확인"
        secureTextEntry={true} // 비밀번호 숨김 처리
        returnKeyType="done"
        ref={changeConfirmPasswordRef}
        value={changeConfirmPassword}
        onChangeText={(text)=>{
          handleConfirmPasswordChangeCheck(text)
        }}
        maxLength={16}
        onSubmitEditing={()=>{
          handleUserPasswordChange()
        }}
      />
      <Text
        style={[styles.pwMessage, {
          color: (isValidPw.isConfirmPwValid) ? "blue" : "red",
        }]}
      >
        {pwMessage.confirmChangePwMessage}
      </Text>
      <View style={{}}>
        <Text style={{
          textAlign:'center',
          color: (isValidPw.isCanChange && isValidPw.responseResult) ? "blue" : "red",
        }}>{pwMessage.responseMessage}</Text>
      </View>
      

      {/* 변경 완료 버튼 */}
      <TouchableOpacity
        style={[styles.changePasswordButton, {backgroundColor:isValidPw.isCanChange?"#FF965C":'grey'}]}
        onPress={() => {
          handleUserPasswordChange()
        }}
        disabled={!isValidPw.isCanChange}
      >
        {/* <Text style={styles.changePasswordButtonText}>변경 완료</Text> */}
        <Text style={styles.changePasswordButtonText}>비밀번호 변경</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 16,
    marginTop: 15,
  },
  arrowIcon: {
    width: 20,
    height: 20,
  },
  iconContainer: {
    alignItems: "center",
    marginTop: 50,
  },
  lockIcon: {
    width: 100,
    height: 100,
    marginBottom: 8,
  },
  passwordInput: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center", // 가로 중앙 정렬
    marginBottom: 16,
  },
  passwordPlaceholder: {
    fontSize: 24,
    borderBottomWidth: 1,
    width: 20, // 각 * 텍스트 너비 설정
    textAlign: "center",
    fontWeight: "bold",
    marginLeft: 10, // 각 * 텍스트 사이 간격 조절
    marginTop: 10,
    marginBottom: 30,
  },
  passwordChangeText: {
    fontSize: 24,
    flex: 1, // 텍스트가 남은 공간을 모두 차지하도록 설정
    textAlign: "center",
  },
  passwordInfoContainer: {
    alignItems: "center",
    marginBottom: 60,
  },
  passwordChangeInfoText: {
    fontSize: 20,
    fontWeight: "bold",
    textAlign: "center",
  },
  passwordInfoText: {
    fontSize: 14,

    color: "#7B7B7B",
  },
  inputBox: {
    borderWidth: 1,
    backgroundColor: "#F5F3F3",
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 6,
    // marginBottom: 16,
    fontSize: 12,
    paddingLeft: 12,
    marginHorizontal: 20,
    width: "90%",
    alignSelf: "center",
  },
  changePasswordButton: {
    backgroundColor: "#FF965C", // 배경색
    borderRadius: 8, // BorderRadius 설정
    alignItems: "center",
    justifyContent: "center",
    height: 40, // 버튼 높이 조절
    marginTop: 16, // 버튼을 아래로 내립니다.
    width: "90%",
    alignSelf: "center",
    // marginTop: 130,
    // marginTop: 100,
  },
  changePasswordButtonText: {
    color: "white", // 텍스트 색상
    fontWeight: "bold", // 텍스트를 bold체로 설정
    fontSize: 15,
  },
  pwMessage:{
    marginTop: 0,
    marginBottom: 10,
    fontSize: 12,
    marginHorizontal: 20
  }
});

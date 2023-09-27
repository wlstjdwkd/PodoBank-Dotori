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
import { userCellPhoneNumberChange} from "../../apis/userapi"

export default function EditPhoneNumberScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const currentPhoneNumberRef = useRef(null)
  const changePhoneNumberRef = useRef(null)

  const [currentPhoneNumber, setCurrentPhoneNumber] = useState(route.params.phoneNumber)
  const [changePhoneNumber, setChangePhoneNumber] = useState("")
  const [isPhoneNumberValid, setIsPhoneNumberValid] = useState(false)
  const [changePhoneNumberMessage, setChangePhoneNumberMessage] = useState("")
  const [responseMessage, setResponseMessage] = useState("")

  const validatePhoneNumber = (text) => {
    const regex = /^01\d{9}$/;
    return regex.test(text);
  };

  const handlePhoneNumberChangeCheck = (text) => {
    setChangePhoneNumber(text)
    if(validatePhoneNumber(text)){
      setIsPhoneNumberValid(true)
      setChangePhoneNumberMessage("핸드폰번호 작성 완료")
      if(responseMessage.length>0){
        setResponseMessage("")
      }
    }else{
      setIsPhoneNumberValid(false)
      setChangePhoneNumberMessage("01X로 시작하는 11자리 숫자로 작성해야 합니다.")
    }
  }

  const handlePhoneNumberChange = () =>{
    if(isPhoneNumberValid === true){
      doUserCellPhoneNumberChange()
    }else{
      setResponseMessage('핸드폰 번호 양식을 확인해주세요.')
    }
  }

  const doUserCellPhoneNumberChange = async () =>{
    const data = {}
    try{
      const response = await userCellPhoneNumberChange(changePhoneNumber, accessToken, grantType)
      if(response.status === 200){
        setResponseMessage("핸드폰 번호 변경 성공")
        console.log('핸드폰 번호 변경 성공')
        Alert.alert('','핸드폰 번호가 변경되었습니다.')
        navigation.navigate("MyPageScreen")
      }else{
        setResponseMessage("오류 발생 : 핸드폰 번호 변경 실패")
        console.log('오류 발생 : 핸드폰 번호 변경 실패', response.status)
      }
    }catch(error){
      console.log('오류발생 : 핸드폰 번호 변경 실패', error)
    }
  }

  useEffect(()=>{
    changePhoneNumberRef.current.focus()
  },[])

  return (
    <View style={styles.container}>
      <HeaderComponent title="핸드폰번호 변경" navigation={navigation}/>

      <View style={styles.iconContainer}>
        <Image
          style={styles.lockIcon}
          // source={require("../../assets/icon/lock.png")} // 프로필 이미지 경로
          source={require("../../assets/icon/smartphone.png")} // 스마트폰 이미지
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
          핸드폰 번호를 변경해주세요.
        </Text>
        <Text style={styles.passwordInfoText}>
          기존 번호를 확인하고, 새 번호를 입력해주세요.
        </Text>
      </View>

      {/* 텍스트 입력란 */}
      <TextInput
        style={[styles.inputBox, {}]}
        placeholder={"기존 번호 : " + currentPhoneNumber}
        placeholderTextColor="black"
        returnKeyType="next"
        keyboardType="number-pad"
        ref={currentPhoneNumberRef}
        // value={currentPhoneNumber}
        onChangeText={(text)=>{
          handlePasswordCurrentCheck(text)
        }}
        maxLength={11}
        onSubmitEditing={()=>{
          changePhoneNumberRef.current.focus()
        }}
        editable={false}
      />
      <Text style={[styles.pwMessage, {}]}></Text>

      <TextInput
        style={styles.inputBox}
        placeholder="변경할 핸드폰 번호 (숫자만 입력)"
        returnKeyType="done"
        keyboardType="number-pad"
        ref={changePhoneNumberRef}
        value={changePhoneNumber}
        onChangeText={(text)=>{
          handlePhoneNumberChangeCheck(text)
        }}
        maxLength={11}
        onSubmitEditing={()=>{
          handlePhoneNumberChange()
        }}
        onFocus={()=>{
          if(!changePhoneNumber){
            setChangePhoneNumber("010")
          }
        }}
      />
      <Text
        style={[styles.pwMessage, {
          color: (isPhoneNumberValid) ? "blue" : "red",
        }]}
      >
        {changePhoneNumberMessage}
      </Text>

      <View style={{}}>
        <Text style={{
          textAlign:'center',
          color: (isPhoneNumberValid) ? "blue" : "red",
        }}>{responseMessage}</Text>
      </View>
      

      {/* 변경 완료 버튼 */}
      <TouchableOpacity
        style={[styles.changePasswordButton, {backgroundColor:isPhoneNumberValid?"#FF965C":'grey'}]}
        onPress={() => {
          handlePhoneNumberChange()
        }}
        disabled={!isPhoneNumberValid}
      >
        <Text style={styles.changePasswordButtonText}>핸드폰 번호 변경</Text>
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

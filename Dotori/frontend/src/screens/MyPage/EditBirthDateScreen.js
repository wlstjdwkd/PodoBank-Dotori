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
import { userBirthdateChange} from "../../apis/userapi"

export default function EditBirthDateScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const currentBirthDateRef = useRef(null)
  const changeBirthDateRef = useRef(null)

  const [currentBirthDate, setCurrentBirthDate] = useState(route.params.birthDate)
  const [changeBirthDate, setChangeBirthDate] = useState("")
  const [isBirthDateValid, setIsBirthDateValid] = useState(false)
  const [changeBirthDateMessage, setChangeBirthDateMessage] = useState("")
  const [responseMessage, setResponseMessage] = useState("")
  

  // const validateBirthDate = (text) => {
  //   const regex = /^01\d{6}$/;
  //   return regex.test(text);
  // };
  const validateBirthDate = (text) => {
    if (!/^\d{8}$/.test(text)) {
      return false; // 8자리 숫자가 아니면 유효하지 않음
    }
    const year = parseInt(text.substr(0, 4));
    const month = parseInt(text.substr(4, 2)) - 1; // 월은 0부터 시작하므로 1을 빼줍니다.
    const day = parseInt(text.substr(6, 2));  
    // Date 객체를 생성하여 유효한 날짜인지 확인합니다.
    const date = new Date(year, month, day);
    // Date 객체의 날짜가 유효하면 (예: 20211231), 유효한 날짜로 간주합니다.
    // Date 객체의 날짜가 유효하지 않으면 (예: 20210231), 유효하지 않은 날짜로 간주합니다.
    return (
      date.getFullYear() === year &&
      date.getMonth() === month &&
      date.getDate() === day
    );
  };

  const handleUserBirthdateChangeCheck = (text) => {
    setChangeBirthDate(text)
    if(validateBirthDate(text)){
      setIsBirthDateValid(true)
      setChangeBirthDateMessage("생년월일 작성 완료")
      if(responseMessage.length>0){
        setResponseMessage("")
      }
    }else{
      setIsBirthDateValid(false)
      setChangeBirthDateMessage("정확한 날짜를 입력해주세요.")
    }
  }

  const handleUserBirthdateChange = async () =>{
    if(isBirthDateValid === true){
      doUserBirthdateChange()
    }else{
      setResponseMessage('생년월일 양식을 확인해주세요.')
    }
  }

  const formattedBirthDate  = (date) =>{
    const year = changeBirthDate.slice(0, 4);
    const month = changeBirthDate.slice(4, 6);
    const day = changeBirthDate.slice(6, 8);
    return `${year}-${month}-${day}`
  }

  const doUserBirthdateChange = async () =>{
    const data = formattedBirthDate(changeBirthDate)
    try{
      const response = await userBirthdateChange(data, accessToken, grantType)
      if(response.status === 200){
        setIsBirthDateValid(true)
        setResponseMessage("생년월일 변경 성공")
        console.log('생년월일 변경 성공')
        Alert.alert('','생년월일이 변경되었습니다.')
        navigation.navigate("MyPageScreen")
      }else{
        setResponseMessage("오류 발생 : 생년월일 변경 실패")
        console.log('오류 발생 : 생년월일 변경 실패', response.status)
        setIsBirthDateValid(false)
      }
    }catch(error){
      console.log('오류발생 : 생년월일 변경 실패', error)
      setIsBirthDateValid(false)
    }
  }

  useEffect(()=>{
    changeBirthDateRef.current.focus()
  },[])

  return (
    <View style={styles.container}>
      <HeaderComponent title="생년월일 변경" navigation={navigation}/>

      <View style={styles.iconContainer}>
        <Image
          style={styles.lockIcon}
          source={require("../../assets/icon/birthday.png")} // 스마트폰 이미지
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
          생년월일을 변경해주세요.
        </Text>
        <Text style={styles.passwordInfoText}>
          기존 생년월일을 확인하고, {"\n"}변경할 생년월일을 입력해주세요.
        </Text>
      </View>

      {/* 텍스트 입력란 */}
      <TextInput
        style={[styles.inputBox, {}]}
        placeholder={"기존 생년월일 : " + currentBirthDate}
        placeholderTextColor="black"
        returnKeyType="next"
        keyboardType="number-pad"
        ref={currentBirthDateRef}
        // value={currentBirthDate}
        onChangeText={(text)=>{
          handlePasswordCurrentCheck(text)
        }}
        onSubmitEditing={()=>{
          changeBirthDateRef.current.focus()
        }}
        editable={false}
      />
      <Text style={[styles.pwMessage, {}]}></Text>

      <TextInput
        style={styles.inputBox}
        placeholder="변경할 생년월일 (숫자만 입력)"
        returnKeyType="done"
        keyboardType="number-pad"
        ref={changeBirthDateRef}
        value={changeBirthDate}
        onChangeText={(text)=>{
          handleUserBirthdateChangeCheck(text)
        }}
        maxLength={8}
        onSubmitEditing={()=>{
          handleUserBirthdateChange()
        }}
        onFocus={()=>{
          if(!changeBirthDate){
            
          }
        }}
      />
      <Text
        style={[styles.pwMessage, {
          color: (isBirthDateValid) ? "blue" : "red",
        }]}
      >
        {changeBirthDateMessage}
      </Text>

      <View style={{}}>
        <Text style={{
          textAlign:'center',
          color: (isBirthDateValid) ? "blue" : "red",
        }}>{responseMessage}</Text>
      </View>
      

      {/* 변경 완료 버튼 */}
      <TouchableOpacity
        style={[styles.changePasswordButton, {backgroundColor:isBirthDateValid?"#FF965C":'grey'}]}
        onPress={() => {
          handleUserBirthdateChange()
        }}
        disabled={!isBirthDateValid}
      >
        <Text style={styles.changePasswordButtonText}>생년월일 변경</Text>
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
    textAlign: 'center',
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

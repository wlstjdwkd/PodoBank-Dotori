import React, { useRef, useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Alert,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";

export default function SignUp4Screen({ navigation, route }) {
  const nameInputRef = useRef(null); // 이름 입력란 ref
  const birthDateInputRef = useRef(null); // 생년월일 입력란 ref
  const phoneNumberInputRef = useRef(null); // 휴대폰번호 입력란 ref

  const [userInfo, setUserInfo] = useState(route.params.userInfo);
  const [nameMessage, setNameMessage] = useState("");
  const [birthDateMessage, setBirthDateMessage] = useState("");
  const [phoneNumberMessage, setPhoneNumberMessage] = useState("");
  // textinput value들
  const [nameValue, setNameValue] = useState("")
  const [birthDateValue, setbBirthDateValue] = useState("")
  const [phoneNumberValue, setPhoneNumberValue] = useState("")

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
    setNameValue(text)
    if (validateName(text)) {
      setNameMessage("이름 작성 완료");
      setIsValid((prev) => ({ ...prev, isNameValid: true }));
      setUserInfo({ ...userInfo, name: text });
    } else {
      setNameMessage("이름은 2글자 이상 음절로 이루어져야 합니다.");
      setIsValid((prev) => ({ ...prev, isNameValid: false }));
      setUserInfo({ ...userInfo, name: "" });
    }
  };
  const handleBirthDateChange = (text) => {
    setbBirthDateValue(text)
    if (validateBirthDate(text)) {
      setBirthDateMessage("생년월일 작성 완료");
      setIsValid((prev) => ({ ...prev, isBirthdateValid: true }));
      setUserInfo({ ...userInfo, birthDate: text });
    } else {
      setBirthDateMessage("8자리 숫자로 작성해야 합니다.");
      setIsValid((prev) => ({ ...prev, isBirthdateValid: false }));
      setUserInfo({ ...userInfo, birthDate: "" });
    }
  };
  const handlePhoneNumberChange = (text) => {
    setPhoneNumberValue(text)
    if (validatePhoneNumber(text)) {
      setPhoneNumberMessage("휴대폰번호 작성 완료");
      setIsValid((prev) => ({ ...prev, isPhoneNumberValid: true }));
      setUserInfo({ ...userInfo, phoneNumber: text });
    } else {
      setPhoneNumberMessage("01X로 시작하는 11자리 숫자로 작성해야 합니다.");
      setIsValid((prev) => ({ ...prev, isPhoneNumberValid: false }));
      setUserInfo({ ...userInfo, phoneNumber: "" });
    }
  };
  
  const gotoSignUpCompleteScreen = () =>{
    switch (true) {
      case !isValid.isNameValid:
        // console.log(isValid)
        Alert.alert('','작성하신 이름을 다시 확인해주세요.')
        nameInputRef.current.focus()
        break;
      case !isValid.isBirthdateValid:
        // console.log(isValid)
        Alert.alert('','작성하신 생년월일을 다시 확인해주세요.')
        birthDateInputRef.current.focus()
        break;
      case !isValid.isPhoneNumberValid:
        // console.log(isValid)
        Alert.alert('','작성하신 휴대폰번호를 다시 확인해주세요.')
        phoneNumberInputRef.current.focus()
        break;
      default:
        console.log(userInfo)
        navigation.navigate("SignUpCompleteScreen", { name: userInfo.name })
        break;
    }
  }


  return (
    <View style={styles.container}>
      <HeaderComponent
        title="회원가입(4/4)"
        cancelNavi="LoginScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <ScrollView style={styles.header}>
          <Text style={styles.title}>가입정보 입력하기</Text>
          <View style={styles.rowContainer}>
            <Text style={styles.subtitle}>이름</Text>
            {/* <Text
              style={{
                color: isValid.isNameValid ? "blue" : "red",
                fontSize : 12,
              }}
            >
              {nameMessage}
            </Text> */}
          </View>

          <TextInput
            style={nameValue ?styles.input:[styles.input,{fontSize:12}]}
            placeholder="예) 박새로이" // 색깔 및 크기 조정
            placeholderTextColor="#7B7B7B"
            onChangeText={handleNameChange}
            // multiline={true}
            keyboardType="default"
            returnKeyType ="next"
            maxLength={8}
            value={nameValue}
            ref={nameInputRef}
            onSubmitEditing={()=>{
              // if(isValid.isNameValid){
              //   birthDateInputRef.current.focus()
              // }
              birthDateInputRef.current.focus()
            }}
          />
          <Text
            style={ isValid.isNameValid ? styles.validMessage1 : styles.validMessage2}
          >
            {nameMessage}
          </Text>

          <View style={styles.rowContainer}>
            <Text style={styles.subtitle}>생년월일</Text>
            {/* <Text
              style={{
                color: isValid.isBirthdateValid ? "blue" : "red",
                fontSize : 12,
                //   marginLeft: 150,'
              }}
            >
              {birthDateMessage}
            </Text> */}
          </View>

          <TextInput
            style={birthDateValue ?styles.input:[styles.input,{fontSize:12}]}
            // style={styles.input}
            onChangeText={handleBirthDateChange}
            // multiline={true}
            placeholder="생년월일 8자리 예) 19991212" // 색깔 및 크기 조정
            placeholderTextColor="#7B7B7B"
            keyboardType="number-pad"
            maxLength={8}
            returnKeyType ="next"
            value = {birthDateValue}
            ref={birthDateInputRef}
            onSubmitEditing={()=>{
              // if(isValid.isBirthdateValid){
              //   phoneNumberInputRef.current.focus()
              // }
              phoneNumberInputRef.current.focus()
            }}
          />
          <Text
            style={ isValid.isBirthdateValid ? styles.validMessage1 : styles.validMessage2}
          >
            {birthDateMessage}
          </Text>

          {/* <Text style={styles.inputBehindText}>
            생년월일 8자리 예) 19991212
          </Text> */}

          <View style={styles.rowContainer}>
            <Text style={styles.subtitle}>휴대폰번호</Text>
            {/* <Text
              style={{
                color: isValid.isPhoneNumberValid ? "blue" : "red",
                fontSize : 12,
                //   marginLeft: 0,
              }}
            >
              {phoneNumberMessage}
            </Text> */}
          </View>

          <TextInput
            style={phoneNumberValue ?styles.input:[styles.input,{fontSize:12}]}
            // style={styles.input}
            onChangeText={handlePhoneNumberChange}
            // multiline={true}
            placeholder="예) 01012345678" // 색깔 및 크기 조정
            placeholderTextColor="#7B7B7B"
            keyboardType="number-pad"
            maxLength={11}
            returnKeyType ="done"
            value = {phoneNumberValue}
            ref={phoneNumberInputRef}
            onFocus={()=>{
              if(!phoneNumberValue){
                setPhoneNumberValue("010")
              }
            }}
            onSubmitEditing={()=>{
              gotoSignUpCompleteScreen()
            }}
          />
          <Text
            style={ isValid.isPhoneNumberValid ? styles.validMessage1 : styles.validMessage2}
          >
            {phoneNumberMessage}
          </Text>
        </ScrollView>
        
        <TouchableOpacity
          style={styles.button}
          onPress={() =>{
            gotoSignUpCompleteScreen()
          }}
        >
          <Text style={styles.buttonText}>회원가입</Text>
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
    marginBottom: 40,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 10,
  },
  input: {
    width: "100%",
    height: 40,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    // marginBottom: 20,
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
    fontSize: 15,
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
  validMessage1:{
    color: "blue",
    marginBottom: 20,
    fontSize: 12,
  },
  validMessage2:{
    color: "red",
    fontSize: 12,
    marginBottom: 20,
  }
});

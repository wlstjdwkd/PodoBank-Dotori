import React, { useState, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  Alert,
} from "react-native";
import FooterScreen from "../Components/FooterScreen";
import { useDispatch, useSelector } from "react-redux";
import {accountVerificationsOnecentCheck} from "../../apis/accountapi"

export default function OneCent2Screen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>{state.user.grantType})
  const accessToken =  useSelector((state)=>{state.user.accessToken})
  const refreshToken =  useSelector((state)=>{state.user.refreshToken})
  const dispatch = useDispatch()
  // 그 외
  
  const [accountInfo, setAccountInfo] = useState(route.params.accountInfo);
  const [numbers, setNumbers] = useState(["", "", "", ""]);
  const inputRefs = [useRef(null), useRef(null), useRef(null), useRef(null)];

  const handleBoxPress = (index) => {
    // 네모 박스를 누르면 해당 칸에 포커스를 줍니다.
    inputRefs[index].current.focus();
  };

  const handleInputChange = (text, index) => {
    if (text === "" && index > 0) {
      inputRefs[index - 1].current.focus();
    }
    // 각 텍스트 입력 상자에 입력된 값을 상태에 업데이트합니다.
    const newNumbers = [...numbers];
    newNumbers[index] = text;
    setNumbers(newNumbers);

    // 입력이 완료되면 다음 칸으로 포커스 이동합니다.
    if (index < 3 && text.length === 1) {
      inputRefs[index + 1].current.focus();
    }
  };

  const doAccountVerificationsOnecentCheck = async () =>{
    const data = {
      bankSeq: accountInfo.bankSeq,
      accountNumber: accountInfo.accountNumber,
      verificationCode: numbers.join("")
    }
    try{
      const response = await accountVerificationsOnecentCheck(data, accessToken, grantType)
      if(response.status === 200){
        Alert.alert('','1원 인증 완료입니다.')
        navigation.navigate("MainPageScreen")
      }else if(response.status === 404){
        console.log('1원 인증 코드 검증 실패',response.status)
      }else{
        console.log('1원 인증 코드 검증 실패',response.status)
      }
    }catch(error){
      console.log('오류 발생 : 1원 인증 코드 검증 실패', error)
    }
  }

  return (
    <View style={styles.container}>
      <View style={styles.innerContainer}></View>
      <Text style={styles.centeredText}>1원을 보냈습니다.</Text>
      <Text style={styles.smallGrayText}>
        입금내역에 표시된 숫자 4자리를 입력해주세요.
      </Text>

      <View style={styles.boxContainer}>
        {numbers.map((value, index) => (
          <TouchableOpacity
            key={index}
            style={styles.numberBox}
            onPress={() => handleBoxPress(index)}
          >
            <Text style={styles.numberText}>{value}</Text>
            <TextInput
              style={styles.input}
              maxLength={1}
              onChangeText={(text) => handleInputChange(text, index)}
              value={value}
              ref={inputRefs[index]}
              keyboardType="numeric"
              onSubmitEditing={() => {
                if (index < 3) {
                  inputRefs[index + 1].current.focus();
                }else if(index === 3){
                  doAccountVerificationsOnecentCheck()
                }
              }}
            />
          </TouchableOpacity>
        ))}
      </View>

      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          doAccountVerificationsOnecentCheck()
        }}
      >
        <Text style={styles.buttonText}>확인</Text>
      </TouchableOpacity>
      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "white",
    padding: 16,
  },
  centeredText: {
    fontSize: 24,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 16,
    marginTop: 160,
  },
  smallGrayText: {
    color: "#A9A9A9",
    fontSize: 16,
    textAlign: "center",
    marginBottom: 32,
  },
  boxContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "80%",
    marginBottom: 32,
  },
  numberBox: {
    width: 64,
    height: 64,
    borderWidth: 2,
    borderColor: "#FF965C",
    borderRadius: 4,
    justifyContent: "center",
    alignItems: "center",
  },
  numberText: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#000000",
  },
  input: {
    position: "absolute",
    width: "100%",
    height: "100%",
    textAlign: "center",
    fontSize: 24,
    fontWeight: "bold",
    color: "#000000",
    backgroundColor: "transparent",
  },
  button: {
    backgroundColor: "#FF965C",
    borderRadius: 8,
    width: "90%",
    // padding: 16,
    height: 40,
    marginTop: 300,
    marginBottom: -100,
  },
  buttonText: {
    fontSize: 15,
    color: "white",
    marginTop: 10,
    textAlign: "center",
    fontWeight: "bold",
  },
  footer: {
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "center",
    marginBottom: -20,
  },
});

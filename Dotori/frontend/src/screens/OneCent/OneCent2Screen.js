import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
} from "react-native";
import FooterScreen from "../Components/FooterScreen";
import { useDispatch, useSelector } from "react-redux";

export default function OneCent1Screen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>{state.user.grantType})
  const accessToken =  useSelector((state)=>{state.user.accessToken})
  const refreshToken =  useSelector((state)=>{state.user.refreshToken})
  const dispatch = useDispatch()
  // 그 외
  
  const [accountInfo, setAccountInfo] = useState({
    bankName: route.params.bankName,
    bankImage: route.params.bankImage,
    accountNumber: "",
  });
  // const [accountNumber, setAccountNumber] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isValid, setIsValid] = useState(false);

  const handleConfirm = () => {
    if (accountInfo.accountNumber.length !== 13) {
      setErrorMessage("계좌 번호를 13자리로 입력해주세요.");
    } else {
      setErrorMessage("");
      // console.log(accountInfo);
      navigation.navigate("OneCent3Screen", {
        accountInfo: accountInfo,
      }); // 계좌 번호를 다음 페이지로 전달
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.innerContainer}>
        <Text style={styles.boldTextLeft}>계좌 번호를 입력해주세요.</Text>

        {/* 텍스트 입력 박스 */}
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="계좌 번호를 입력하세요."
            placeholderTextColor="#A9A9A9"
            underlineColorAndroid="transparent" // 하단 선 숨기기
            keyboardType="numeric" // 숫자 키패드 표시
            maxLength={13} // 최대 13자리로 제한
            textAlign="center" // 가운데 정렬
            value={accountInfo.accountNumber}
            onChangeText={(text) => {
              setAccountInfo({ ...accountInfo, accountNumber: text });

              // setAccountNumber(text);
              if (text.length !== 13) {
                setErrorMessage("계좌 번호를 13자리로 입력해주세요.");
              } else {
                setErrorMessage("");
              }
            }}
          />
        </View>

        {/* 오류 메시지 */}
        {errorMessage !== "" && (
          <Text style={styles.errorMessage}>{errorMessage}</Text>
        )}

        {/* 버튼 */}
        <TouchableOpacity style={styles.button} onPress={handleConfirm}>
          <Text style={styles.buttonText}>확인</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // alignItems: "flex-start",
    // justifyContent: "center",
    backgroundColor: "white",
    padding: 16,
  },
  innerContainer: {
    flex: 1,
    // alignItems: "flex-start",
    // justifyContent: "center",
    backgroundColor: "white",
    padding: 16,
  },
  boldTextLeft: {
    fontWeight: "bold",
    fontSize: 24,
    marginBottom: 120,
    textAlign: "left",
    marginTop: 150,
  },
  inputContainer: {
    width: "100%",
    borderBottomWidth: 1, // 하단 선 추가
    borderColor: "#FF965C", // 선 색상 설정
    marginBottom: 10,
  },
  input: {
    width: "100%",
    fontSize: 16,
    paddingVertical: 8,
    color: "#000000",
  },
  button: {
    backgroundColor: "#FF965C",
    borderRadius: 8,
    width: "100%",
    padding: 10,
    alignItems: "center",
    marginTop: 35,
  },
  buttonText: {
    fontSize: 15,
    color: "white",
    fontWeight: "bold",
  },
  errorMessage: {
    color: "red",
    fontSize: 16,
    textAlign: "center",
    marginTop: 8,
  },
  footer: {
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "center",
    marginBottom: -20,
  },
});

import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native";

import HeaderComponent from "../Header/HeaderScreen";

export default function SignupInformationScreen({ navigation, route }) {
  const [userInfo, setUserInfo] = useState(route.params.userInfo);
  const [authenEmail, SetAuthenEmail] = useState(false);
  const [authenEmailNum, SetAuthenEmailNum] = useState(""); // 6자리의 인증번호라고 가정
  const [isAuthenEmail, SetIsAuthenEmail] = useState(false); // 임시로 true로 유지중
  const [confirmAuthenEmailNum, setConfirmAuthenEmailNum] = useState("");

  // function compareAuthenNum() {
  //   // 차후 백에서 정받아와서 비교 해서 true로 바꿀 것
  //   SetIsAuthenEmail(true)
  // }
  function compareAuthenNum() {
    const backCompare = 111111;
    if (authenEmailNum != backCompare) {
      setConfirmAuthenEmailNum("잘못된 인증번호입니다.");
      SetIsAuthenEmail(false);
    } else {
      setConfirmAuthenEmailNum("인증이 완료되었습니다.");
      SetIsAuthenEmail(true);
    }
  }

  return (
    <View style={styles.container}>
      {/* Header */}
      <HeaderComponent
        navigation={navigation}
        title="회원가입(2/3)"
      ></HeaderComponent>

      {/* 본인 인증 안내 */}
      {/* <Text style={styles.boldText}>계정 정보를 입력해주세요</Text> */}
      <Text style={styles.boldText}>이메일을 입력해주세요</Text>

      {/* 이메일 입력창 */}
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>이메일</Text>
        <View style={styles.inputRowContainer}>
          <TextInput
            style={[styles.input]}
            onChangeText={(text) =>
              setUserInfo((prev) => ({ ...prev, email: text }))
            }
            placeholder="ID로 사용될 이메일을 선택해주세요."
            keyboardType="email-address"
          />
          <TouchableOpacity
            style={styles.verifyButton}
            onPress={() => {
              SetAuthenEmail(true);
            }}
          >
            <Text style={styles.verifyButtonText}>전송</Text>
          </TouchableOpacity>
        </View>
      </View>
      {/* 이메일 인증번호 입력창 */}
      {authenEmail ? (
        <View style={[styles.inputContainer, { align: "flex-end" }]}>
          {/* <Text style={styles.inputText}>이메일 인증</Text> */}
          <View style={styles.inputRowContainer}>
            <TextInput
              style={styles.input}
              onChangeText={(text) => SetAuthenEmailNum(text)}
              placeholder="인증번호를 입력해주세요."
              keyboardType="number-pad" // 숫자로만 받는다는 가정하에 넘버패드로 받음
              maxLength={6}
            />
            <TouchableOpacity
              // 차후 백과의 연동에서 인증번호 일치할때만 제대로 작동
              style={[
                styles.verifyButton,
                !(userInfo.email && authenEmailNum.length == 6) && {
                  backgroundColor: "grey",
                },
              ]}
              disabled={!(userInfo.email && authenEmailNum.length == 6)}
              onPress={compareAuthenNum}
            >
              <Text style={styles.verifyButtonText}>인증</Text>
            </TouchableOpacity>
          </View>
        </View>
      ) : null}
      <Text
        style={{
          color: isAuthenEmail ? "blue" : "red",
          marginLeft: 30,
          marginTop: -30,
        }}
      >
        {confirmAuthenEmailNum}
      </Text>

      <TouchableOpacity
        style={[
          styles.customButton,
          !isAuthenEmail && {
            backgroundColor: "grey",
          },
        ]}
        // back에 회원가입 정보 보내야함
        onPress={() =>
          navigation.navigate("SignupInformationScreen", { userInfo: userInfo })
        }
        disabled={!isAuthenEmail}
      >
        <Text style={styles.linkText}>다음</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
    paddingTop: 20,
    backgroundColor: "white",
  },
  textRow: {
    flexDirection: "row",
    alignItems: "center",
  },

  boldText: {
    fontWeight: "bold",
    fontSize: 20,
    marginTop: 30,
    marginBottom: 30,
    marginRight: 30,
    marginLeft: 30,
  },
  inputText: {
    marginLeft: 30,
  },
  descriptionText: {
    color: "grey",
    marginLeft: 10,
    fontSize: 12,
  },
  inputContainer: {
    marginTop: 10,
    justifyContent: "flex-start",
  },
  inputRowContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  verifyButton: {
    backgroundColor: "#A175FD",
    borderRadius: 10,
    paddingVertical: 15,
    paddingHorizontal: 15,
    marginLeft: -10,
    height: 50,
    marginRight: 30,
    marginTop: -20,
    justifyContent: "center",
  },
  verifyButtonText: {
    color: "black",
    // marginRight: 20,
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderRadius: 10,
    paddingLeft: 10,
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    marginBottom: 30,
    height: 50,
    textAlignVertical: "center",

    // 그림자 스타일 추가
    elevation: 5,
    backgroundColor: "white",
  },
  customButton: {
    backgroundColor: "#A175FD",
    padding: 10,
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 30,
    marginRight: 30,
    marginLeft: 30,
    borderRadius: 5,
  },
});

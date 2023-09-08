import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import HeaderComponent from "../Header/HeaderScreen";

export default function SignupIdentityVerificationScreen({ navigation }) {
  const [userInfo, setUserInfo] = useState({
    name: "",
    birthDate: "",
    phoneNumber: "",
  });

  return (
    <View style={styles.container}>
      {/* Header */}
      <HeaderComponent title="회원가입(1/2)" navigation={navigation}></HeaderComponent>

      {/* 본인 인증 안내 */}
      <Text style={styles.boldText}>본인 인증을 진행해주세요</Text>

      {/* 이름 입력창 */}
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>이름</Text>
        <TextInput
          style={styles.input}
          onChangeText={(text) => setUserInfo({ ...userInfo, name: text })}
          value={userInfo.name}
        />
      </View>
      <View style={styles.inputContainer}>
        <Text
          style={styles.inputText}
          onChangeText={(text) => setUserInfo({ ...userInfo, birthDate: text })}
          value={userInfo.birthDate}
        >
          생년월일 8자리
        </Text>
        <TextInput style={styles.input} />
      </View>
      <View style={styles.inputContainer}>
        <Text
          style={styles.inputText}
          onChangeText={(text) =>
            setUserInfo({ ...userInfo, phoneNumber: text })
          }
          value={userInfo.phoneNumber}
        >
          휴대폰번호
        </Text>
        <TextInput style={styles.input} />
      </View>
      <TouchableOpacity
        style={styles.customButton}
        onPress={() =>
          navigation.navigate("SignupInformationScreenEmail", { userInfo: userInfo })
        }
      >
        <Text style={styles.linkText}>확인</Text>
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
  inputContainer: {
    marginTop: 10,
    justifyContent: "flex-start",
  },
  input: {
    borderWidth: 1,
    borderRadius: 10,
    paddingLeft: 10,
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    marginBottom: 30,
    height: 50,
    textAlignVertical: "center",
    elevation: 3,
    backgroundColor: "white",
  },
  customButton: {
    backgroundColor: "#A175FD",
    padding: 10,
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    borderRadius: 5,
  },
});

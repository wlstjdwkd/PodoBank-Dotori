import React from "react";
import {
  View,
  Text,
  Image,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native";

export default function SignupIdentityVerificationScreen({ navigation }) {
  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.headerContainer}>
        <Image
          source={require("../../assets/images/normal_podo.png")}
          style={styles.podoImage}
        />
        <Text style={styles.bankText}>포도은행</Text>
        <Text style={styles.signupText}>회원가입</Text>
      </View>

      {/* 본인 인증 안내 */}
      <Text style={styles.boldText}>본인 인증을 진행해주세요</Text>

      {/* 이름 입력창 */}
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>이름</Text>
        <TextInput style={styles.input} />
      </View>
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>생년월일 8자리</Text>
        <TextInput style={styles.input} />
      </View>
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>휴대폰번호</Text>
        <TextInput style={styles.input} />
      </View>
      <TouchableOpacity
        style={styles.customButton}
        onPress={() => navigation.navigate("SignupInformationScreen")}
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
  headerContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "flex-start",
    paddingTop: 30,
  },
  podoImage: {
    width: 25,
    height: 25,
    resizeMode: "contain",
    marginLeft: 30,
    marginRight: 15,
  },
  bankText: {
    fontSize: 15,
    marginRight: 15,
  },
  signupText: {
    color: "grey",
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
    // flexDirection: "row",
    // alignItems: "center",
    justifyContent: "flex-start",
  },
  input: {
    borderWidth: 1,
    borderRadius: 10,
    // flex: 1,
    paddingLeft: 10,
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    marginBottom: 30,
    height: 50,
    textAlignVertical: "center",
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

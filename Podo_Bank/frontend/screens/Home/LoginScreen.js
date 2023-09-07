import React from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  Image,
} from "react-native";
import { AntDesign } from "@expo/vector-icons";

export default function LoginScreen({ navigation }) {
  return (
    <View style={styles.container}>
      {/* Logo */}
      <View style={styles.logoContainer}>
        <Image
          source={require("../../assets/images/podo_bank_name.png")}
          style={styles.logoImage}
        />
        {/* 로고 이미지 로드 */}
      </View>

      {/* ID, PW 입력창 */}
      <View style={styles.inputContainer}>
        <AntDesign name="user" size={20} style={styles.iconStyle} />
        <TextInput placeholder="아이디" style={styles.input} />
      </View>
      <View style={styles.inputContainer}>
        <AntDesign name="lock" size={20} style={styles.iconStyle} />
        <TextInput
          placeholder="비밀번호"
          style={styles.input}
          secureTextEntry={true}
        />
      </View>

      {/* 로그인 버튼 */}
      {/* 로그인 back 연동해야함 */}
      <TouchableOpacity
        style={styles.customButton}
        onPress={() => navigation.navigate("HomeScreen")}
      >
        <Text style={styles.buttonText}>로그인</Text>
      </TouchableOpacity>

      {/* 회원가입 */}
      <View style={styles.linkContainer}>
        <TouchableOpacity onPress={() => navigation.navigate("FindIDScreen")}>
          <Text style={styles.linkText}>아이디 찾기</Text>
        </TouchableOpacity>
        <Text style={styles.separator}> | </Text>
        <TouchableOpacity
          onPress={() => navigation.navigate("FindPasswordScreen")}
        >
          <Text style={styles.linkText}>비밀번호 찾기</Text>
        </TouchableOpacity>
        <Text style={styles.separator}> | </Text>
        <TouchableOpacity
          onPress={() =>
            navigation.navigate("SignupIdentityVerificationScreen")
          }
        >
          <Text style={styles.linkText}>회원가입</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
const styles = StyleSheet.create({
  logoImage: {
    width: 300,
    height: 300,
    resizeMode: "contain", // 'cover'나 'contain' 중 하나를 선택하여 이미지의 크기 조절 방식을 선택
  },
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
    backgroundColor: "transparent",
  },
  logoContainer: {
    alignItems: "center",
    marginBottom: 80,
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
  buttonText: {
    color: "white",
    fontSize: 16,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "grey",
    marginTop: 10,
    marginRight: 30,
    marginBottom: 10,
    marginLeft: 30,
    borderRadius: 5,
  },
  iconStyle: {
    marginLeft: 10,
    marginRight: 10,
  },
  input: {
    flex: 1,
    height: 40,
  },
  linkContainer: {
    flexDirection: "row",
    marginTop: 15,
    justifyContent: "center",
  },
  separator: {
    marginHorizontal: 10,
  },
});

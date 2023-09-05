import React from "react";
import {
  View,
  Text,
  Image,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native";

export default function SignupInformationScreen({ navigation }) {
  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.headerContainer}>
        <Image
          source={require("../../assets/images/normal_podo.png")}
          style={styles.podoImage}
        />
        <Text style={styles.bankText}>포도은행</Text>
      </View>

      {/* 본인 인증 안내 */}
      <Text style={styles.boldText}>계정 정보를 입력해주세요</Text>

      {/* 이름 입력창 */}
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>이메일</Text>
        <View style={styles.inputRowContainer}>
          <TextInput
            style={styles.input}
            placeholder="ID로 사용될 이메일을 선택해주세요."
          />
          <TouchableOpacity style={styles.verifyButton}>
            <Text style={styles.verifyButtonText}>인증하기</Text>
          </TouchableOpacity>
        </View>
      </View>
      <View style={styles.inputContainer}>
        <View style={styles.textRow}>
          <Text style={styles.inputText}>비밀번호</Text>
          <Text style={styles.descriptionText}>
            영문, 숫자, 특수문자가 모두 들어간 8~16글자
          </Text>
        </View>

        <TextInput
          style={styles.input}
          placeholder="비밀번호를 입력해 주세요."
        />
      </View>
      <View style={styles.inputContainer}>
        <Text style={styles.inputText}>비밀번호 확인</Text>
        <TextInput
          style={styles.input}
          placeholder="비밀번호를 한번 더 입력해 주세요."
        />
      </View>
      <TouchableOpacity
        style={styles.customButton}
        onPress={() => navigation.navigate("SignupCompleteScreen")}
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
  textRow: {
    flexDirection: "row",
    alignItems: "center",
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
  descriptionText: {
    color: "grey",
    marginLeft: 10, // '비밀번호'와의 간격 조정
    fontSize: 12, // 폰트 사이즈 조정 (원하는대로 변경 가능)
  },
  inputContainer: {
    marginTop: 10,
    // flexDirection: "row",
    // alignItems: "center",
    justifyContent: "flex-start",
  },
  inputRowContainer: {
    flexDirection: "row",
    alignItems: "center",
  },
  verifyButton: {
    backgroundColor: "#A175FD",
    borderRadius: 10,
    paddingVertical: 15,
    paddingHorizontal: 15,
    marginLeft: -10, // 인풋과 버튼 사이의 간격 조절
    height: 50,
    marginTop: -20,
  },
  verifyButtonText: {
    color: "black",
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

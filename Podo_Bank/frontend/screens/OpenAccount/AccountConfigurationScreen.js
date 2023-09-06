import React, { useState, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
} from "react-native";
import HeaderComponent from "../Header/HeaderScreen";

export default function AccountConfigurationScreen({ navigation }) {
  const [selectedOption, setSelectedOption] = useState(null);
  const [selectedOption2, setSelectedOption2] = useState(null);
  const [password, setPassword] = useState("");
  const passwordInputRef = useRef(null);
  return (
    <View style={styles.container}>
      <HeaderComponent navigation={navigation} title="계좌개설" />
      <View style={styles.purpleBox}>
        <Text style={styles.blackText}>포도은행 입출금통장</Text>
      </View>
      <TouchableOpacity
        style={styles.whiteBox}
        onPress={() => passwordInputRef.current.focus()} // 여기서 focus
      >
        <Text style={styles.blackText}>통장 비밀번호 만들기</Text>
        <View style={styles.passwordDotsContainer}>
          {[...Array(4)].map((_, index) => (
            <View
              key={index}
              style={[
                styles.passwordDot,
                index < password.length ? styles.filledDot : {}, // 입력된 길이에 따른 스타일 적용
              ]}
            />
          ))}
        </View>
        <TextInput
          ref={passwordInputRef} // 참조 설정
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            opacity: 0,
            width: "100%",
            height: "100%",
          }} // 완전히 투명하게 설정
          value={password}
          onChangeText={setPassword}
          maxLength={4}
          keyboardType="numeric"
          secureTextEntry={true}
        />
      </TouchableOpacity>
      <Text style={{ marginBottom: 30, fontSize: 12 }}>
        고객님의 자산을 안전하게 보호하고 전화 금융사기의 피해를 예방하고자
        금융거래목적에 대해 질문드립니다.
      </Text>
      <Text style={{ marginBottom: 15 }}>
        어떤 용도로 통장을 사용하실 건가요?
      </Text>
      <View style={styles.whiteBox}>
        <Text>계좌사용용도</Text>
        <Text style={styles.grayText}>선택해주세요</Text>
      </View>
      <Text style={{ marginBottom: 20 }}>
        타인으로부터 통장대여 요청을 받은 사실이 있나요?
      </Text>
      <View style={styles.radioContainer}>
        <TouchableOpacity
          style={styles.radioButton}
          onPress={() => setSelectedOption("yes")}
        >
          <Text style={selectedOption === "yes" ? styles.checkIcon : null}>
            {selectedOption === "yes" ? "✔" : ""}
          </Text>
        </TouchableOpacity>
        <Text>예</Text>
        <View style={{ marginRight: 160 }} />
        <TouchableOpacity
          style={styles.radioButton}
          onPress={() => setSelectedOption("no")}
        >
          <Text style={selectedOption === "no" ? styles.checkIcon : null}>
            {selectedOption === "no" ? "✔" : ""}
          </Text>
        </TouchableOpacity>
        <Text>아니요</Text>
      </View>

      <Text style={{ marginBottom: 20 }}>
        타인으로부터 신용점수 상향, 대출 등의 목적으로 통장개설을 요청받은
        사실이 있나요?
      </Text>
      <View style={styles.radioContainer}>
        <TouchableOpacity
          style={styles.radioButton}
          onPress={() => setSelectedOption2("yes")}
        >
          <Text style={selectedOption2 === "yes" ? styles.checkIcon : null}>
            {selectedOption2 === "yes" ? "✔" : ""}
          </Text>
        </TouchableOpacity>
        <Text>예</Text>
        <View style={{ marginRight: 160 }} />
        <TouchableOpacity
          style={styles.radioButton}
          onPress={() => setSelectedOption2("no")}
        >
          <Text style={selectedOption2 === "no" ? styles.checkIcon : null}>
            {selectedOption2 === "no" ? "✔" : ""}
          </Text>
        </TouchableOpacity>
        <Text>아니요</Text>
      </View>

      <TouchableOpacity
        style={styles.confirmButton}
        onPress={() => navigation.navigate("AccountRestrictionScreen")}
      >
        <Text style={styles.confirmText}>다음</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    padding: 20,
  },
  purpleBox: {
    backgroundColor: "#8B0FD750",
    padding: 20,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 15,
    borderRadius: 5,
  },
  // blackText: {
  //   color: "white",
  //   fontSize: 20,
  //   textAlign: "center",
  // },
  checkIcon: {
    fontSize: 18,
    color: "black",
    marginTop: -5,
    marginBottom: -5,
  },
  whiteBox: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    backgroundColor: "white",
    padding: 15,
    marginBottom: 15,
    borderColor: "#ddd",
    borderWidth: 1,
    marginBottom: 50,
  },
  blackText: {
    fontSize: 18,
  },
  grayText: {
    fontSize: 18,
    color: "gray",
    // textAlign: "left",
  },
  passwordDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: "gray",
    margin: 2,
  },
  filledDot: {
    backgroundColor: "black",
  },
  passwordInput: {
    borderColor: "black",
    borderWidth: 1,
    width: 90,
    textAlign: "center",
    fontSize: 18,
  },
  passwordDotsContainer: {
    flexDirection: "row",
    justifyContent: "flex-end",
  },
  radioContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 30,
  },
  radioButton: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: "black",
    alignItems: "center",
    justifyContent: "center",
    marginRight: 10,
  },
  innerRadio: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: "black",
  },
  confirmButton: {
    backgroundColor: "#842DC480",
    padding: 15,
    alignItems: "center",
    // borderTopLeftRadius: 5, // 위쪽 왼쪽 모서리만 둥글게
    // borderTopRightRadius: 5, // 위쪽 오른쪽 모서리만 둥글게
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
  },
});

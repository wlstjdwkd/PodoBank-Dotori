import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  Button,
  Dimensions,
  TouchableOpacity,
  Modal,
  Alert,
} from "react-native";

import HeaderScreen from "../Header/HeaderScreen";

const { width } = Dimensions.get("window");

export default function TransferDetailScreen({ route, navigation }) {
  const [receiverMemo, setReceiverMemo] = useState("");
  const [senderMemo, setSenderMemo] = useState("");
  const [modalVisible, setModalVisible] = useState(false);
  const [password, setPassword] = useState("");

  const { amount, receiverBank, receiverAccount } = route.params;

  const handleTransfer = () => {
    // 여기서 이체 로직을 처리합니다.
    console.log("Transfer completed!");
  };

  const onNumberPress = (num) => {
    if (password.length < 4) {
      setPassword(password + num);
    }
  };

  const onBackspacePress = () => {
    setPassword(password.slice(0, -1));
  };

  return (
    <View style={styles.container}>
      <HeaderScreen navigation={navigation} title="이체하기" />
      <Text style={styles.amountText}>{amount}</Text>

      <Text style={styles.label}>받는분</Text>
      <Text style={styles.accountInfo}>
        {receiverBank} {receiverAccount}
      </Text>
      <View style={styles.separator} />

      <Text style={styles.label}>받는 분 통장 표시</Text>
      <TextInput
        style={styles.input}
        placeholder="받는 분 통장 표시"
        onChangeText={setReceiverMemo}
        value={receiverMemo}
        textAlign="center"
      />
      <View style={styles.separator} />

      <Text style={styles.label}>내 통장 표시</Text>
      <TextInput
        style={styles.input}
        placeholder="내 통장 표시"
        onChangeText={setSenderMemo}
        value={senderMemo}
        textAlign="center"
      />
      <View style={styles.separator} />

      <Button
        title="다음"
        color="purple"
        onPress={() => setModalVisible(true)}
      />
      <Modal animationType="slide" transparent={true} visible={modalVisible}>
        <View style={styles.centeredView}>
          <View style={styles.modalView}>
            <TextInput
              style={styles.passwordInput}
              placeholder="계좌 비밀번호를 입력해주세요"
              secureTextEntry={true}
              value={password}
              onChangeText={setPassword}
            />
            {Array(3)
              .fill(0)
              .map((_, rowIndex) => (
                <View key={rowIndex} style={styles.row}>
                  {Array(3)
                    .fill(0)
                    .map((_, colIndex) => {
                      const num = rowIndex * 3 + colIndex + 1;
                      return (
                        <TouchableOpacity
                          key={num}
                          style={styles.numberButton}
                          onPress={() => onNumberPress(num.toString())}
                        >
                          <Text style={styles.numberText}>{num}</Text>
                        </TouchableOpacity>
                      );
                    })}
                </View>
              ))}
            <View style={styles.row}>
              <View style={styles.numberButton} />
              <TouchableOpacity
                style={styles.numberButton}
                onPress={() => onNumberPress("0")}
              >
                <Text style={styles.numberText}>0</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.numberButton}
                onPress={onBackspacePress}
              >
                <Text style={styles.numberText}>←</Text>
              </TouchableOpacity>
            </View>
            <Button
              title="확인"
              color="purple"
              onPress={() => {
                setModalVisible(false);
                handleTransfer();
              }}
            />
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },
  backButton: {
    fontSize: 24,
    fontWeight: "bold",
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: "bold",
  },
  amountText: {
    fontSize: 24,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    marginBottom: 10,
  },
  accountInfo: {
    fontSize: 16,
    marginBottom: 10,
  },
  separator: {
    height: 1,
    backgroundColor: "grey",
    marginBottom: 20,
    width: width - 40,
  },
  input: {
    height: 40,
    borderColor: "transparent",
    borderBottomColor: "grey",
    borderWidth: 1,
    marginBottom: 20,
    width: width - 40,
  },
  centeredView: {
    flex: 1,
    justifyContent: "flex-end",
    alignItems: "center",
    marginTop: 22,
  },
  modalView: {
    width: "100%",
    backgroundColor: "white",
    padding: 20,
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
  },
  passwordInput: {
    borderBottomWidth: 1,
    borderBottomColor: "grey",
    marginBottom: 20,
    textAlign: "center",
    fontSize: 18,
  },
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  numberButton: {
    width: 60,
    height: 60,
    justifyContent: "center",
    alignItems: "center",
    borderRadius: 30,
    borderWidth: 1,
    borderColor: "grey",
  },
  numberText: {
    fontSize: 24,
  },
});

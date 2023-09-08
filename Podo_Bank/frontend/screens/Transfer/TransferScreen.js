import React, { useState, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  Button,
  TouchableOpacity,
  TextInput,
  Modal,
  Animated,
  Dimensions,
  Image,
} from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import { Ionicons } from "@expo/vector-icons";
import HeaderScreen from "../Header/HeaderScreen";

const { height, width } = Dimensions.get("window");

// 더미 데이터 (실제 데이터와 연동 필요)
const recentAccounts = [
  {
    profileImage: require("../../assets/images/normal_podo.png"),
    name: "홍길동",
    accountNumber: "1234-5678-9101",
    bankName: "포도은행",
  },
  {
    profileImage: require("../../assets/images/normal_podo.png"),
    name: "방진성",
    accountNumber: "1234-5678-3101",
    bankName: "포도은행",
  },
  // ... 필요하면 더 추가
];

export default function TransferScreen({ navigation }) {
  const [modalVisible, setModalVisible] = useState(false);
  // const translateY = useRef(new Animated.Value(height)).current;
  const [accountInput, setAccountInput] = useState("");

  const [receiverBank, setReceiverBank] = useState("포도은행");
  const [availableAmount, setAvailableAmount] = useState(0); // 출금 가능 금액 state 추가

  const removeLast = () => {
    setAccountInput((prevAccount) => prevAccount.slice(0, -1));
  };

  const appendAccount = (num) => {
    // if (password.length < 4) {
    setAccountInput((prevAccount) => prevAccount + num);
    // }
  };

  const handleKeyPress = (key) => {
    if (key === "<-") {
      setAccountInput((prev) => prev.slice(0, -1));
    } else {
      setAccountInput((prev) => prev + key);
    }
  };

  // const openModal = () => {
  //   setModalVisible(true);
  //   Animated.timing(translateY, {
  //     toValue: height * 0.4,
  //     duration: 500,
  //     useNativeDriver: true,
  //   }).start();
  // };

  // const closeModal = () => {
  //   Animated.timing(translateY, {
  //     toValue: height,
  //     duration: 500,
  //     useNativeDriver: true,
  //   }).start(() => {
  //     setModalVisible(false);
  //   });
  // };

  return (
    <View style={styles.container}>
      <View style={styles.headerContainer}>
        <HeaderScreen navigation={navigation} title="이체"></HeaderScreen>
      </View>
      <Text style={styles.leftAlignLabel}>포도은행</Text>
      <Text style={styles.boldLeftAlignLabel}>1235-4568-4532</Text>
      <Text style={styles.leftAlignLabel}>
        출금가능금액 {availableAmount}원
      </Text>

      <Text style={styles.receiverLabel}>받는분</Text>

      <TextInput placeholder="포도은행" style={styles.bankLabel}></TextInput>
      <View style={styles.line} />

      <TouchableOpacity
        style={styles.row}
        onPress={() => setModalVisible(true)}
      >
        <Text style={styles.backGrayText}>계좌번호</Text>
      </TouchableOpacity>

      <View style={styles.line} />
      <Text style={styles.centerLabel}>최근 보낸 계좌</Text>
      <View style={styles.line} />
      {recentAccounts.map((account, index) => (
        <TouchableOpacity
          key={index}
          onPress={() => {
            setReceiverBank(account.bankName);
            setAccountInput(account.accountNumber);
          }}
        >
          <View style={styles.accountItem}>
            <Image source={account.profileImage} style={styles.profileImage} />
            <Text style={styles.accountName}>{account.name}</Text>
          </View>
          <View style={styles.accountItem}>
            <Text style={styles.accountDetail}>{account.bankName}</Text>
            <Text style={styles.accountNumber}>{account.accountNumber}</Text>
          </View>
          <View style={styles.line} />
        </TouchableOpacity>
      ))}
      <Modal animationType="slide" transparent={true} visible={modalVisible}>
        <View style={styles.centeredView}>
          <View style={styles.modalContainer}>
            <View style={styles.passwordHeader}>
              <Text style={styles.passwordLabelText}>계좌번호</Text>
              <TouchableOpacity onPress={() => setModalVisible(false)}>
                <Text style={styles.closeButtonText}>X</Text>
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.passwordInput}
              placeholder="계좌번호 입력"
              value={accountInput}
              onChangeText={setAccountInput}
            />

            <View style={styles.numPad}>
              {[
                ["1", "2", "3"],
                ["4", "5", "6"],
                ["7", "8", "9"],
                ["", "0", "<-"],
              ].map((row, rowIndex) => (
                <View key={rowIndex} style={styles.numRow}>
                  {row.map((num) => (
                    <TouchableOpacity
                      key={num}
                      style={styles.numButton}
                      onPress={() => {
                        if (num === "<-") {
                          removeLast();
                        } else if (num !== "") {
                          appendAccount(num);
                        }
                      }}
                    >
                      <Text style={styles.numText}>{num}</Text>
                    </TouchableOpacity>
                  ))}
                </View>
              ))}
            </View>

            <TouchableOpacity
              style={styles.confirmButton}
              onPress={() => {
                setModalVisible(false);
                navigation.navigate("TransferAmountScreen", {
                  receiverBank: receiverBank,
                  accountInput: accountInput,
                });
              }}
            >
              <Text style={styles.confirmButtonText}>확인</Text>
            </TouchableOpacity>
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
    paddingLeft: 30,
    paddingRight: 30,
  },
  headerContainer: {
    marginLeft: -10,
    marginRight: -10,
  },
  centeredView: {
    flex: 1,
    justifyContent: "flex-end",
    alignItems: "center",
  },
  modalContainer: {
    width: "100%",
    height: "70%",
    backgroundColor: "white",
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    padding: 20,
    paddingLeft: 30,
    paddingRight: 30,
    justifyContent: "space-between",
  },
  passwordHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 15,
  },
  passwordLabelText: {
    fontSize: 15,
    // fontWeight: "bold",
  },
  passwordInput: {
    borderBottomWidth: 1,
    borderBottomColor: "#8F1414",
    marginBottom: 20,
    textAlign: "center",
    fontSize: 18,
  },
  profileImage: {
    width: 16,
    height: 16,
    borderRadius: 20,
    marginRight: 10,
  },
  leftAlignLabel: {
    fontSize: 16,
    color: "#5A5A5A",
    textAlign: "left",
  },
  boldLeftAlignLabel: {
    fontSize: 16,
    textAlign: "left",
    fontWeight: "bold",
  },
  receiverLabel: {
    fontSize: 16,
    marginTop: 30,
  },
  backButton: {
    fontSize: 24,
    fontWeight: "bold",
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: "bold",
  },
  bankLabel: {
    fontSize: 16,
    textAlign: "left",
    marginTop: 20,
    marginBottom: 10,
  },
  input: {
    height: 40,
    fontSize: 16,
    borderColor: "transparent",
    borderBottomColor: "grey",
    borderWidth: 1,
    marginBottom: 10,
  },
  backGrayText: {
    color: "grey",
    height: 40,
    fontSize: 16,
  },
  line: {
    height: 1,
    backgroundColor: "grey",
    marginBottom: 20,
  },
  centerLabel: {
    fontSize: 16,
    textAlign: "center",
    marginTop: 100,
    marginBottom: 20,
  },

  accountItem: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 5,
  },
  accountName: {
    fontSize: 18,
    marginBottom: 5,
  },
  accountDetail: {
    fontSize: 16,
    marginRight: 10,
    marginLeft: 25,
    color: "gray",
  },
  accountNumber: {
    fontSize: 16,
    color: "gray",
  },
  closeButtonText: {
    fontSize: 24,
  },
  numPad: {
    flex: 1,
    justifyContent: "center",
  },
  numRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  numButton: {
    width: width / 3 - 40,
    height: 60,
    justifyContent: "center",
    alignItems: "center",
    borderColor: "#e0e0e0",
    borderRadius: 5,
    color: "#8F1414",
  },
  numText: {
    fontSize: 40,
  },
  confirmButton: {
    backgroundColor: "#842DC480",
    padding: 15,
    borderRadius: 5,
    alignSelf: "stretch",
    alignItems: "center",
    marginLeft: -30,
    marginRight: -30,
    marginBottom: -20,
  },
  confirmButtonText: {
    color: "white",
    fontSize: 18,
  },
});

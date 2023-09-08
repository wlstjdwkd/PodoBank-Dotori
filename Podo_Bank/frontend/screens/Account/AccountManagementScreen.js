import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Modal,
  TextInput,
  Dimensions,
} from "react-native";
import { Ionicons, Feather } from "@expo/vector-icons";
import HeaderComponent from "../Header/HeaderScreen";

const { width } = Dimensions.get("window");

export default function AccountManagementScreen({ navigation }) {
  const copyAccountNumber = () => {
    // 클립보드로 복사하는 로직 추가 필요
  };
  const [modalVisible, setModalVisible] = useState(false);
  const [secondModalVisible, setSecondModalVisible] = useState(false);

  const [password, setPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");

  const removeLast = () => {
    setPassword((prevPassword) => prevPassword.slice(0, -1));
  };

  const appendAmount = (num) => {
    if (password.length < 4) {
      setPassword((prevPassword) => prevPassword + num);
    }
  };

  const newRemoveLast = () => {
    setNewPassword((prevPassword) => prevPassword.slice(0, -1));
  };

  const newAppendAmount = (num) => {
    if (password.length < 4) {
      setNewPassword((prevPassword) => prevPassword + num);
    }
  };

  return (
    <View style={styles.container}>
      {/* Header */}
      <HeaderComponent title="거래내역조회" navigation={navigation} />

      <Text style={styles.bankName}>포도은행 통장</Text>
      <View style={styles.accountRow}>
        <Text style={styles.boldText}>1235-4568-4532</Text>
        <TouchableOpacity
          onPress={copyAccountNumber}
          style={{ marginLeft: 10 }}
        >
          <Feather name="copy" size={24} color="gray" />
        </TouchableOpacity>
      </View>

      <View style={styles.frontText}>
        <View style={styles.row}>
          <Text style={styles.grayText}>잔액</Text>
          <Text>1,000,000,000원</Text>
        </View>

        <View style={styles.row}>
          <Text style={styles.grayText}>출금가능금액</Text>
          <Text>1,000,000,000원</Text>
        </View>

        <View style={styles.row}>
          <Text style={styles.grayText}>개설일</Text>
          <Text>2017.08.04</Text>
        </View>
      </View>

      <View style={styles.separator} />

      <Text style={styles.management}>관리하기</Text>

      <TouchableOpacity style={styles.row}>
        <Text style={styles.backGrayText}>입출금 알림(Push)</Text>
        <Ionicons name="chevron-forward" size={24} color="black" />
      </TouchableOpacity>

      <View style={styles.separator} />

      <TouchableOpacity style={styles.row}>
        <Text style={styles.backGrayText}>자동이체</Text>
        <Text>0건</Text>
        <Ionicons name="chevron-forward" size={24} color="black" />
      </TouchableOpacity>

      <View style={styles.separator} />

      <TouchableOpacity
        style={styles.row}
        onPress={() => setModalVisible(true)}
      >
        <Text style={styles.backGrayText}>계좌비밀번호 변경/오류 해제</Text>
        <Ionicons name="chevron-forward" size={24} color="black" />
      </TouchableOpacity>

      <TouchableOpacity>
        <Text style={styles.deleteAccountText}>
          계좌를 해지하시려면 여기를 눌러주세요.
        </Text>
      </TouchableOpacity>
      <Modal animationType="slide" transparent={true} visible={modalVisible}>
        <View style={styles.centeredView}>
          <View style={styles.modalContainer}>
            <View style={styles.passwordHeader}>
              <Text style={styles.passwordLabelText}>현재비밀번호</Text>
              <TouchableOpacity onPress={() => setModalVisible(false)}>
                <Text style={styles.closeButtonText}>X</Text>
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.passwordInput}
              placeholder="비밀번호(4자리) 입력"
              secureTextEntry={true}
              value={password}
              onChangeText={setPassword}
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
                          appendAmount(num);
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
                setSecondModalVisible(true);
              }}
            >
              <Text style={styles.confirmButtonText}>확인</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal
        animationType="slide"
        transparent={true}
        visible={secondModalVisible}
      >
        <View style={styles.centeredView}>
          <View style={styles.modalContainer}>
            <View style={styles.passwordHeader}>
              <Text style={styles.passwordLabelText}>새로운 비밀번호</Text>
              <TouchableOpacity onPress={() => setSecondModalVisible(false)}>
                <Text style={styles.closeButtonText}>X</Text>
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.passwordInput}
              placeholder="비밀번호(4자리) 입력"
              secureTextEntry={true}
              value={newPassword}
              onChangeText={setNewPassword}
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
                          newRemoveLast();
                        } else if (num !== "") {
                          newAppendAmount(num);
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
                setSecondModalVisible(false);
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
  },

  header: {
    alignItems: "center",
    marginBottom: 20,
  },
  frontText: {
    marginBottom: 30,
  },
  bankName: {
    textAlign: "left",
    fontSize: 16,
    marginBottom: 10,
  },
  boldText: {
    fontWeight: "bold",
    fontSize: 20,
  },
  grayText: {
    color: "gray",
  },
  backGrayText: {
    color: "gray",
    fontSize: 20,
  },
  accountRow: {
    flexDirection: "row",
    alignItems: "center",
    // justifyContent: "space-between",
    marginBottom: 40,
  },
  row: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: 10,
  },
  separator: {
    borderBottomColor: "gray",
    borderBottomWidth: 1,
    marginBottom: 10,
  },
  management: {
    fontWeight: "bold",
    fontSize: 18,
    marginBottom: 15,
    marginTop: 15,
  },
  deleteAccountText: {
    color: "gray",
    fontSize: 12,
  },
  centeredView: {
    flex: 1,
    justifyContent: "flex-end",
    alignItems: "center",
  },
  modalContainer: {
    width: "100%",
    height: "60%",
    backgroundColor: "white",
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    padding: 20,
    justifyContent: "space-between",
  },
  closeButtonView: {
    alignSelf: "flex-end",
  },
  closeButtonText: {
    fontSize: 24,
  },
  profileArrowsViewHorizontal: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    marginVertical: 20,
  },
  profileImage: {
    width: 30,
    height: 30,
    borderRadius: 15,
    borderWidth: 1,
    borderColor: "black",
  },
  arrowsContainer: {
    flexDirection: "row",
    marginHorizontal: 10,
  },
  arrowText: {
    fontSize: 18,
    marginHorizontal: 5,
  },
  transferText: {
    fontSize: 18,
    marginTop: 15,
    textAlign: "center",
    fontWeight: "bold",
  },
  amountText: {
    fontSize: 18,
    marginTop: 15,
    textAlign: "center",
  },
  bankInfoText: {
    fontSize: 16,
    marginTop: 15,
    marginBottom: 30,
    textAlign: "center",
    color: "grey",
  },
  confirmButton: {
    backgroundColor: "purple",
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
  },
  numText: {
    fontSize: 40,
  },
  passwordHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 15,
  },
  passwordLabelText: {
    fontSize: 18,
    fontWeight: "bold",
  },
  passwordInput: {
    borderBottomWidth: 1,
    borderBottomColor: "grey",
    marginBottom: 20,
    textAlign: "center",
    fontSize: 18,
  },
});

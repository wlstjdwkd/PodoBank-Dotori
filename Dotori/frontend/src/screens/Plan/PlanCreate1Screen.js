import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
  TouchableWithoutFeedback,
  Modal,
} from "react-native";
import { AntDesign } from "@expo/vector-icons";
import { Calendar, markedDates } from "react-native-calendars";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";

export default function PlanCreate1Screen({ navigation, route }) {
  // 토큰
  // const grantType = useSelector((state) => {
  //   state.user.grantType;
  // });
  // const accessToken = useSelector((state) => {
  //   state.user.accessToken;
  // });
  // const refreshToken = useSelector((state) => {
  //   state.user.refreshToken;
  // });
  // const dispatch = useDispatch();
  // 그 외

  const [planInfo, setPlanInfo] = useState({
    startedAt: null,
    endAt: null,
    accountSeq: route.params.accountSeq,
  });

  const [isCalendarVisible, setCalendarVisible] = useState(false);
  const [isValid, setIsValid] = useState(false);
  console.log(planInfo);
  const markedDates = {
    ...(planInfo.startedAt
      ? {
          [planInfo.startedAt]: { selected: true, color: "blue" },
        }
      : {}),
    ...(planInfo.endAt
      ? {
          [planInfo.endAt]: { selected: true, color: "red" },
        }
      : {}),
  };

  const handleDateChange = (day) => {
    setPlanInfo((prevPlanInfo) => {
      if (!prevPlanInfo.startedAt) {
        return {
          ...prevPlanInfo,
          startedAt: day.dateString,
        };
      } else if (!prevPlanInfo.endAt) {
        return {
          ...prevPlanInfo,
          endAt: day.dateString,
        };
      }
      return prevPlanInfo;
    });
    console.log(planInfo);
    setIsValid(true);
  };

  const handleConfirmDates = () => {
    setCalendarVisible(false);
  };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="계획 생성(1/5)"
        cancelNavi="PlanMainScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <View style={styles.header}>
          <Text style={styles.title}>목표 기간 설정하기</Text>
          <Text style={styles.subtitle}>
            목표 시작 날짜와 종료 날짜를 선택해주세요.
          </Text>

          <Text style={styles.dateText}>시작 날짜</Text>
          <TouchableWithoutFeedback
            onPress={() => {
              setPlanInfo({
                ...planInfo,
                startedAt: null,
                endAt: null,
              });
              setCalendarVisible(true);
            }}
          >
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                value={planInfo.startedAt}
                multiline={true}
              />
              <AntDesign name="calendar" size={24} color="black" />
            </View>
          </TouchableWithoutFeedback>

          <Text style={styles.dateText}>종료 날짜</Text>
          <TouchableWithoutFeedback
            onPress={() => {
              setPlanInfo({
                ...planInfo,
                endAt: null,
              });
              setCalendarVisible(true);
            }}
          >
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                value={planInfo.endAt}
                multiline={true}
              />
              <AntDesign name="calendar" size={24} color="black" />
            </View>
          </TouchableWithoutFeedback>
          {isCalendarVisible && (
            <Modal
              animationType="slide"
              transparent={false}
              visible={isCalendarVisible}
              onRequestClose={() => {
                setCalendarVisible(false);
              }}
            >
              <View style={{ flex: 1, justifyContent: "center" }}>
                <Calendar
                  onDayPress={(day) => handleDateChange(day)}
                  markedDates={markedDates}
                />
                <TouchableOpacity
                  style={styles.button}
                  onPress={handleConfirmDates}
                >
                  <Text style={styles.buttonText}>다음</Text>
                </TouchableOpacity>
              </View>
            </Modal>
          )}
        </View>

        <TouchableOpacity
          style={styles.button}
          onPress={() =>
            navigation.navigate("PlanCreate2Screen", {
              planInfo: planInfo,
            })
          }
          //TODO: 풀기
          // disabled={!isValid}
        >
          <Text style={styles.buttonText}>다음</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  innerContainer: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    marginTop: 90,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 60,
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    marginTop: 10,
    marginBottom: 40,
    backgroundColor: "#D9D9D920",
    position: "relative",
  },
  input: {
    flex: 1,
    height: 20,
    padding: 0, // remove padding to avoid overlap
    fontSize: 16,
    // textAlign: "center",
  },
  button: {
    height: 50,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
  dateText: {
    fontSize: 18,
  },
});

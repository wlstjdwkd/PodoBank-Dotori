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

export default function PurposeCreate2Screen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>{state.user.grantType})
  const accessToken =  useSelector((state)=>{state.user.accessToken})
  const refreshToken =  useSelector((state)=>{state.user.refreshToken})
  const dispatch = useDispatch()
  // 그 외

  const [purposeInfo, setPurposeInfo] = useState(route.params.purposeInfo);
  const [selectedDates, setSelectedDates] = useState({
    startDate: null,
    endDate: null,
  });
  const [isCalendarVisible, setCalendarVisible] = useState(false);
  const [settingDateType, setSettingDateType] = useState(""); // "start" 또는 "end"
  const [isValid, setIsValid] = useState(false);
  const handleDateChange = (day) => {
    if (!selectedDates.startDate) {
      setSelectedDates({
        ...selectedDates,
        startDate: day.dateString,
      });
    } else if (!selectedDates.endDate) {
      setSelectedDates({
        startDate: selectedDates.startDate,
        endDate: day.dateString,
      });
      setIsValid(true);
    }
  };
  const getDatesBetweenDates = (startDate, endDate) => {
    let dates = [];
    const currDate = new Date(startDate);
    const lastDate = new Date(endDate);
    while (currDate <= lastDate) {
      dates.push(currDate.toISOString().split("T")[0]);
      currDate.setDate(currDate.getDate() + 1);
    }
    return dates;
  };
  const markedDates = {
    ...(selectedDates.startDate
      ? {
          [selectedDates.startDate]: { selected: true, color: "blue" },
        }
      : {}),
    ...(selectedDates.endDate
      ? {
          [selectedDates.endDate]: { selected: true, color: "red" },
        }
      : {}),
    ...(purposeInfo.startDate
      ? {
          [purposeInfo.startDate]: { selected: true, color: "blue" },
        }
      : {}),
    ...getDatesBetweenDates(
      selectedDates.startDate,
      selectedDates.endDate
    ).reduce((acc, date) => {
      acc[date] = { selected: true, color: "green" };
      return acc;
    }, {}),
    ...getDatesBetweenDates(purposeInfo.startDate, purposeInfo.endDate).reduce(
      (acc, date) => {
        acc[date] = { selected: true, color: "green" };
        return acc;
      },
      {}
    ),
  };
  const handleConfirmDates = () => {
    setPurposeInfo({
      ...purposeInfo,
      startDate: selectedDates.startDate,
      endDate: selectedDates.endDate,
    });
    setSelectedDates({
      startDate: null,
      endDate: null,
    });
    setCalendarVisible(false);
  };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="목표생성(2/3)"
        cancelNavi="PurposeScreen"
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
              setSettingDateType("start");
              setSelectedDates({
                startDate: null,
                endDate: null,
              });
              setCalendarVisible(true);
            }}
          >
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                value={purposeInfo.startDate}
                multiline={true}
              />
              <AntDesign name="calendar" size={24} color="black" />
            </View>
          </TouchableWithoutFeedback>

          <Text style={styles.dateText}>종료 날짜</Text>
          <TouchableWithoutFeedback
            onPress={() => {
              setSettingDateType("end");
              setSelectedDates({
                startDate: null,
                endDate: null,
              });
              setCalendarVisible(true);
            }}
          >
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                value={purposeInfo.endDate}
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
            navigation.navigate("PurposeCreate3Screen", {
              purposeInfo: purposeInfo,
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
    height: 40,
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

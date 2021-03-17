//
//  ViewController.swift
//  phone
//
//  Created by Federico Lagarmilla on 3/16/21.
//

import UIKit
import core

class ViewController: UIViewController {
    @IBOutlet weak var lblMessage: UILabel!
    var messagesPresenter: MessagesPresenter?
    var messages: [Message] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setup()
    }

    func setup() {
        let locator = LocatorHelper()
        locator.setUp(databaseDriverFactory: DatabaseDriverFactory())
        messagesPresenter = MessagesPresenter(
            repository: locator.getRepository(),
            messagesClient: locator.getClient()
        )
        loadMessages()
        sendMessage()
    }

    func loadMessages() {
        guard let mPresenter = messagesPresenter else { return }
        mPresenter.getMessagesHelper().watch { [weak self] (messages) in
            if let messages = messages as? [Message] {
                self?.lblMessage.text = messages.first?.message
                self?.messages = messages
            } else {
                self?.messages = []
            }
            
        }
    }
    
    func sendMessage() {
        guard let mPresenter = messagesPresenter else { return }
        mPresenter.sendMessage(message: "testing")
    }
}


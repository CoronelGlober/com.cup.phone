//
//  ViewController.swift
//  phone
//
//  Created by Federico Lagarmilla on 3/16/21.
//

import UIKit
import core

class ViewController: UIViewController {
    var messagesPresenter: MessagesPresenter?
    var messages: [Message] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setup()
    }

    func setup() {
        Locator().setUp(databaseDriverFactory: DatabaseDriverFactory())
        messagesPresenter = MessagesPresenter(
            repository: Locator().getRepository(),
            messagesClient: Locator().getClient()
        )
        loadMessages()
    }

    func loadMessages() {
        guard let mPresenter = messagesPresenter else { return }
        mPresenter.getMessagesHelper().watch { [weak self] (messages) in
            if let messages = messages as? [Message] {
                self?.messages = messages
            } else {
                self?.messages = []
            }
            
        }
    }


}

